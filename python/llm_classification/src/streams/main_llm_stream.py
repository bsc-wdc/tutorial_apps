import sys
import subprocess
import os
from copy import deepcopy
import signal
import pandas as pd
from pprint import pprint
import numpy as np
from collections import defaultdict

from ollama import Client

from pycompss.api.task import task
from pycompss.api.constraint import constraint
from pycompss.api.parameter import STREAM_IN
from pycompss.api.parameter import STREAM_OUT
from pycompss.streams.distro_stream import ObjectDistroStream

CTX_SIZE = 8192

def start_ollama(cwd):
    gpu_id = int(os.environ['CUDA_VISIBLE_DEVICES'])
    host_port = f'127.0.0.1:{11434+gpu_id}'

    environ = deepcopy(os.environ)
    environ['OLLAMA_MODELS'] = cwd + '/models'
    environ['OLLAMA_FLASH_ATTENTION'] = '1'
    environ['OLLAMA_HOST'] = host_port
    environ['OLLAMA_NUM_PARALLEL'] = '1'

    # These envars need to be deleted because they make ollama use the CPU only
    if 'GPU_DEVICE_ORDINAL' in environ:
        del environ['GPU_DEVICE_ORDINAL']
    if 'ROCR_VISIBLE_DEVICES' in environ:
        del environ['ROCR_VISIBLE_DEVICES']

    proc = subprocess.Popen(['./ollm/bin/ollama', 'serve'], env=environ, cwd=cwd)
    return proc, host_port

@constraint(processors=[{'processorType':'CPU', 'computingUnits':'1'},
                        {'processorType':'GPU', 'computingUnits':'1'}])
@task(stream_docs=STREAM_IN, stream_class=STREAM_OUT)
def ollama_task(cwd, stream_docs, stream_class, CATEGORIES):
    import pyextrae.multiprocessing as pyextrae

    ollama_proc, host_port = start_ollama(cwd)

    client = Client(host=host_port)

    # LLM PROMPT
    gen_prompt = lambda new_doc: f'{new_doc}\nGiven this article your task is just to classify it into one of the following categories: {", ".join(CATEGORIES)}.'
    
    llm_config = {
        'num_ctx': CTX_SIZE,    # Number of tokens to keep in context (increases memory usage)
        'temperature': 0.,      # Introduces randomness in the generation process. Represents the `variability` of the output
        'num_predict': 10,      # Maximum number of tokens to predict, our classes are short 
    }

    while not stream_docs.is_closed():
        for idx, new_doc in stream_docs.poll():
            pyextrae.eventandcounters(9100000, 2)
            prompt = gen_prompt(new_doc)

            resp = client.generate(model='qwen3:14b', stream=False, prompt=prompt, 
                                   think=False, options=llm_config)
            
            stream_class.publish((idx, resp.response.strip().lower()))
            pyextrae.eventandcounters(9100000, 0)


    ollama_proc.send_signal(signal.SIGINT)
    ollama_proc.wait()


if __name__ == '__main__':
    import pyextrae.sequential as pyextrae

    if len(sys.argv) < 3:
        print('ERROR: Bad number of parameters')
        print(f'Usage: {sys.argv[0]} <NUM_GPUS> <BBC_DATASET_PATH>')
        sys.exit(1)

    NUM_GPUS = int(sys.argv[1])
    DATASET_PATH = sys.argv[2]

    # LOAD DATASET
    bbc_df = pd.read_csv(DATASET_PATH)

    # EXTRACT THE UNIQUE CLASSES
    categories = bbc_df['category'].unique().tolist()

    cwd = os.getcwd()

    # CREATE INPUT AND OUTPUT STREAMS
    stream_docs = ObjectDistroStream()
    stream_class = ObjectDistroStream()
    
    # START OLLAMA SERVER FOR EACH GPU
    for gid in range(NUM_GPUS):
        ollama_task(cwd, stream_docs, stream_class, categories)

    # SEND NEWS ARTICLES TO THE OLLAMA TASKS
    for i, news in enumerate(bbc_df['text'].tolist()):
        stream_docs.publish((i, news))

    # GET THE PREDICTED CLASSES
    results = []
    while len(results) < len(bbc_df):
        recv = stream_class.poll()
        for idx, class_predicted in recv:
            pyextrae.eventandcounters(9100000, 1)
            true_class = bbc_df['category'].tolist()[idx]
            results.append([true_class, class_predicted])
            pyextrae.eventandcounters(9100000, 0)

    
    # COMPUTE ACCURACY
    true_classes = bbc_df['category'].tolist()
    categories_correct = defaultdict(int)
    for y_true, y_pred in results:
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

    with open('classification_results_streams.txt', 'w') as f:
        for i, (pred_class, true_class) in enumerate(results):
            f.write(f'{i}: {pred_class} ({true_class})\n')

    # CLOSE STREAMS and OLLAMA
    stream_docs.close()
    stream_class.close()