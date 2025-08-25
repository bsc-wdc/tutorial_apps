# LLM Document Classification using PyCOMPSs

[Contact Person]: support-compss@bsc.es
[Access Level]: public
[License Agreement]: Apache2
[Platform]: COMPSs
[Execution Environment]: Supercomputer (MN5-ACC)

This folder contains all PyCOMPSs related tutorial notebooks.

It is divided into two main folders:

## Description

This tutorial app shows how to use PyCOMPSs in combination with ollama to perform document classification using a pre-trained LLM model. The application uses [Ollama](https://ollama.com/) inference server to deploy Qwen3:14B on multiple GPUs using PyCOMPSs and classify [BBC News Topic Dataset](https://huggingface.co/datasets/SetFit/bbc-news). It consisting of 2.225 articles published on the BBC News website where each one is labeled under one of 5 categories: business, entertainment, politics, sport or tech. Using this workflow, each article text will be classified in parallel using PyCOMPSs orchestration mechanism.

The application has been divided into 2 versions:

**Streams**: Uses PyCOMPSs streams to send, classify and retrieve the data in real-time between tasks. This version needs PyCOMPSs to be installed with Kafka component.
**Objects**: Uses default PyCOMPSs mechanism to transfer data between tasks at their creation and completion. 

Since LLM models are memory intensive and require GPUs for reasonable performance, this application has been developed to run on clusters equipped with GPUs. It has been tested on MareNostrum5-ACC.

## Prerequisites

* Python
* COMPSs installation
* pandas
* numpy
* [ollama-python](https://github.com/ollama/ollama-python)
* [ollama server](https://ollama.com/)
* [qwen3:14b](https://ollama.com/library/qwen3:14b)

## Execution instructions

Usage:
    bash objects/launch_llm.sh <NUM_NODES> <DATASET_PATH> <OLLAMA_MODEL_PATH>  
    or  
    bash streams/launch_llm_stream.sh <NUM_NODES> <DATASET_PATH> <OLLAMA_MODEL_PATH>

where:  
- NUM_NODES: Number of cluster nodes to use for the job.  
- DATASET_PATH: Absolute path of the BBC News CSV dataset.  
- OLLAMA_MODEL_PATH: Absolute path of Qwen3:14B directory.  


== Execution Examples ==  
`bash objects/launch_llm.sh 2 $(pwd)/dataset/bbc-text.csv $(pwd)/models/`
