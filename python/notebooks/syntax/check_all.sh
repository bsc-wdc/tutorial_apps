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
    run_notebook "$(basename "$file")"
done
