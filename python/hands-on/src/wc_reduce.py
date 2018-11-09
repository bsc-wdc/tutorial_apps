#!/usr/bin/python
# -*- coding: utf-8 -*-
'''Wordcount files - worker read'''
import sys
import os
import time
from pycompss.api.task import task
from pycompss.api.parameter import *


@task(returns=list)
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


@task(returns=dict)
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


@task(returns=dict, priority=True)
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
    from pycompss.api.api import compss_wait_on

    # Get the dataset path
    pathDataset = sys.argv[1]

    # Construct a list with the file's paths from the dataset
    paths = []
    for fileName in os.listdir(pathDataset):
        paths.append(os.path.join(pathDataset, fileName))

    # Start counting time...
    startTime = time.time()

    # Read file's content
    data = []
    for p in paths:
        data.append(read_file(p))

    # From all file's data execute a wordcount on it
    partialResult = []
    for d in data:
        partialResult.append(wordCount(d))

    # Accumulate the partial results to get the final result.
    result = {}
    for partial in partialResult:
        result = merge_two_dicts(result, partial)


    # Wait for result
    result = compss_wait_on(result)

    # Print results and ellapsed time
    print "Ellapsed Time (s)"
    print time.time()-startTime

    print "Words: {}".format(sum(result.values()))