#!/usr/bin/python
#
#  Copyright 2002-2019 Barcelona Supercomputing Center (www.bsc.es)
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#

# -*- coding: utf-8 -*-

import sys
import os
import time


def read_file(file_path):
    """ Read a file and return a list of words.
    :param file_path: file's path
    :return: list of words
    """
    data = []
    with open(file_path, 'r') as f:
        for line in f:
            data += line.split()
    return data


def wordCount(data):
    """ Construct a frequency word dictorionary from a list of words.
    :param data: a list of words
    :return: a dictionary where key=word and value=#appearances
    """
    partialResult = {}
    for entry in data:
        if entry in partialResult:
            partialResult[entry] += 1
        else:
            partialResult[entry] = 1
    return partialResult


def merge_two_dicts(dic1, dic2):
    """ Update a dictionary with another dictionary.
    :param dic1: first dictionary
    :param dic2: second dictionary
    :return: dic1+=dic2
    """
    for k in dic2:
        if k in dic1:
            dic1[k] += dic2[k]
        else:
            dic1[k] = dic2[k]
    return dic1


if __name__ == "__main__":
    # Start counting time...
    start_time = time.time()

    # Get the dataset path
    pathDataset = sys.argv[1]

    # Construct a list with the file's paths from the dataset
    paths = []
    for fileName in os.listdir(pathDataset):
        paths.append(os.path.join(pathDataset, fileName))

    # Read file's content execute a wordcount on each of them
    partialResult = []
    for p in paths:
        data = read_file(p)
        partialResult.append(wordCount(data))

    # Accumulate the partial results to get the final result.
    result = {}
    for partial in partialResult:
        result = merge_two_dicts(result, partial)

    elapsed_time = time.time() - start_time

    # Print the results and elapsed time
    print("Word appearances:")
    from pprint import pprint
    pprint(result)
    print("Elapsed Time (s): " + str(elapsed_time))
    print("Words: " + str(sum(result.values())))
