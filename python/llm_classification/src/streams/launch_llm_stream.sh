module load tensorflow
module unload openmpi impi
module load COMPSs/3.3.3

NUM_NODES=$1
DATASET_PATH=$2
OLLAMA_BIN_PATH=$3
OLLAMA_MODEL_PATH=$4

export OLLAMA_MODELS=$OLLAMA_MODEL_PATH

enqueue_compss \
    --num_nodes=$NUM_NODES \
    --exec_time=20 \
    --tracing=true \
    --project_name=bsc19 \
    --log_dir=$(pwd) \
    --streaming=OBJECTS \
    --master_working_dir=$(pwd) \
    --worker_working_dir=$(pwd) \
    --qos=acc_debug \
    --pythonpath=$(pwd) \
    --python_interpreter=$(which python3) \
    --lang=python main_llm_stream.py $((NUM_NODES * 4)) $DATASET_PATH $OLLAMA_BIN_PATH
