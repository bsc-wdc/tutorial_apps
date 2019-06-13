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

""" Words generator """
import sys
import os
from loremipsum import *


if __name__ == "__main__":
    numFiles = int(sys.argv[1])
    size = int(sys.argv[2])*1024*1024  # Mbytes to bytes
    pathDataset = "dataset_{}f_{}mb".format(numFiles, (size/1024)/1024)
    os.mkdir(pathDataset)

    for i in xrange(numFiles):
        sizeText = 0
        text = ""
        fileName = "file{}.txt".format(i)
        path = os.path.join(pathDataset, fileName)
        with open(path, 'w') as f:
            while sizeText < size:
                paragraph = generate_paragraph()
                f.write(paragraph[2])
                sizeText = os.path.getsize(path)
        print "{} generated: size {} mb".format(fileName, (float(sizeText)/1024.0)/1024.0)
