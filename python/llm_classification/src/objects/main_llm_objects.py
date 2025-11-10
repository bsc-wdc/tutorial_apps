import sys
import subprocess
import os
from copy import deepcopy
import numpy as np
import pandas as pd
from time import sleep
from collections import defaultdict
import socket
from pprint import pprint

from ollama import Client

from pycompss.api.task import task
from pycompss.api.constraint import constraint
from pycompss.api.api import compss_wait_on

PARALLEL_QUERIES = 8
CTX_SIZE = 8192

@constraint(processors=[{'processorType':'CPU', 'computingUnits':'1'},
                        {'processorType':'GPU', 'computingUnits':'4'}])
@task(is_distributed=True)
def start_ollama(OLLAMA_BIN_PATH):
    hostname = socket.gethostname()
    host_ports = []
    for gpu_id_str in os.environ['CUDA_VISIBLE_DEVICES'].split(','):
        gpu_id = int(gpu_id_str)
        port = 11434+gpu_id
        host_port = f'0.0.0.0:{port}'

        environ = deepcopy(os.environ)
        environ['OLLAMA_FLASH_ATTENTION'] = '1'
        environ['OLLAMA_HOST'] = host_port
        environ['OLLAMA_NUM_PARALLEL'] = str(PARALLEL_QUERIES)
        environ['CUDA_VISIBLE_DEVICES'] = str(gpu_id)

        # These envars need to be deleted because they make ollama use the CPU only
        if 'GPU_DEVICE_ORDINAL' in environ:
            del environ['GPU_DEVICE_ORDINAL']
        if 'ROCR_VISIBLE_DEVICES' in environ:
            del environ['ROCR_VISIBLE_DEVICES']

        subprocess.Popen([OLLAMA_BIN_PATH, 'serve'], env=environ)

        host_ports.append(f'{hostname}:{port}')

    sleep(10)
    return host_ports


# `is_replicated=True` runs the task in every node by just calling it once
@task(is_replicated=True)
def close_ollama():
    os.system("killall ollama")

@constraint(computingUnits='PARALLEL_QUERIES')
@task()
def query_prompt(host_port, prompt, system_prompt=''):
    client = Client(host=host_port, timeout=60.0)
    llm_config = {
        'num_ctx': CTX_SIZE,    # Number of tokens to keep in context (increases memory usage)
        'temperature': 0.,      # Introduces randomness in the generation process. Represents the `variability` of the output
        'num_predict': 10,      # Maximum number of tokens to predict, our classes are short 
    }
    resp = client.generate(model='qwen3:14b', stream=False, prompt=prompt, think=False,
                        options=llm_config, system=system_prompt)
    return resp.response.strip().lower()

if __name__ == '__main__':
    if len(sys.argv) < 4:
        print('ERROR: Bad number of parameters')
        print(f'Usage: {sys.argv[0]} <NUM_NODES> <BBC_DATASET_PATH> <OLLAMA_BIN_PATH>')
        sys.exit(1)

    NUM_NODES = int(sys.argv[1])
    DATASET_PATH = sys.argv[2]
    OLLAMA_BIN_PATH = sys.argv[3]

    # LOAD DATASET
    bbc_df = pd.read_csv(DATASET_PATH)

    # EXTRACT THE UNIQUE CLASSES
    categories = bbc_df['category'].unique().tolist()

    cwd = os.getcwd()
    
    # START OLLAMA SERVER FOR EACH GPU
    hosts_address = compss_wait_on([start_ollama(cwd) for _ in range(NUM_NODES)])
    hosts_address = [item for sublist in hosts_address for item in sublist]

    # LLM PROMPTs
    system_prompt = f"You are a document classifier. Your only task is to identify the correct category for the given document. Respond only with one of these categories: {', '.join(categories)}"
    gen_prompt = lambda new_doc: f"Document: {new_doc}"

    # CONCURRENT CLASSIFICATION OF PROMPTS, EACH GPU PROCESS 8 DOCUMENTS
    num_servers = len(hosts_address)
    pred_classes = []
    for i, new_doc in enumerate(bbc_df['text'].tolist()):
        y_pred = query_prompt(hosts_address[i % num_servers], gen_prompt(new_doc), system_prompt)
        pred_classes.append(y_pred)
    pred_classes = compss_wait_on(pred_classes)
    
    # COMPUTE ACCURACY
    true_classes = bbc_df['category'].tolist()
    categories_correct = defaultdict(int)
    for y_true, y_pred in zip(true_classes, pred_classes):
        correct = 1 if y_true == y_pred else 0
        categories_correct[y_true] += correct
    labels, label_counts = np.unique(true_classes, return_counts=True)

    performance_dict = {}
    for i, label in enumerate(labels):
        acc = (categories_correct[label] / label_counts[i]) * 100
        performance_dict[label] = f'{acc:.2f}%'

    # PRINT AND SAVE RESULTS
    print('************* CLASSIFICATION ACCURACY RESULTS *************')
    pprint(performance_dict)
    print('**********************************************************')

    with open('classification_results_objects.txt', 'w') as f:
        for i, (pred_class, true_class) in enumerate(zip(pred_classes, true_classes)):
            f.write(f'{i}: {pred_class} ({true_class})\n')

    # CLOSE OLLAMA IN ALL NODES
    close_ollama()