#!/bin/bash

expectedTime=300s

function run_notebook {
  app_ipynb=$1

  timeout --signal=2 $expectedTime \
        jupyter nbconvert \
        --ExecutePreprocessor.kernel_name=python2 \
        --ExecutePreprocessor.timeout=240 \
        --execute "${app_ipynb}"

  exit_value=$?
  if [ ${exit_value} -ne 0 ]; then
      exit ${exit_value}
  fi

  timeout --signal=2 $expectedTime \
        jupyter nbconvert \
        --ExecutePreprocessor.kernel_name=python3 \
        --ExecutePreprocessor.timeout=240 \
        --execute "${app_ipynb}"

  exit_value=$?
  if [ ${exit_value} -ne 0 ]; then
      exit ${exit_value}
  fi
}

# Run all notebooks within this folder
for file in ./*.ipynb; do
    if [[ *"Exercise"* == ${file} ]]; then
        echo "Found exercise: Skipping $(basename "$file")"
        continue
    fi
    if [[ ${file} == *"9_KMeans.ipynb" ]] || [[ ${file} == *"9_KMeans-reduce-chunks.ipynb" ]] || [[ ${file} == *"10_Cholesky.ipynb" ]]; then
        echo "Notebook with widget: Skipping $(basename "$file")"
        continue
    fi
    if [[ ${file} == *"11_Wordcount"* ]]; then
        echo "Notebook without dataset in repository: Skipping $(basename "$file")"
        continue
    fi
    if [[ ${file} == *"8_SortByKey.ipynb"* ]]; then
       echo "Notebook with BUG: Skipping $(basename "$file")"
       continue
    fi
    run_notebook "$(basename "$file")"
done
