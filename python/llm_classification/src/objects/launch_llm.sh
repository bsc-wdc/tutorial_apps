module load tensorflow
module unload openmpi impi
module load COMPSs/3.3.3

NUM_NODES=$1
DATASET_PATH=$2
OLLAMA_MODEL_PATH=$3

export OLLAMA_MODELS=$OLLAMA_MODEL_PATH

enqueue_compss \
    --num_nodes=$NUM_NODES \
    --exec_time=20 \
    --tracing=true \
    --project_name=bsc19 \
    --log_dir=$(pwd) \
    --master_working_dir=$(pwd) \
    --worker_working_dir=$(pwd) \
    --qos=acc_debug \
    --pythonpath=$(pwd) \
    --python_interpreter=$(which python3) \
    --lang=python main_llm_objects.py $NUM_NODES $DATASET_PATH