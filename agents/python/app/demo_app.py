#!/usr/bin/python
#
#  Copyright 2002-2025 Barcelona Supercomputing Center (www.bsc.es)
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
import numpy as np

from pycompss.api.task import task


@task()
def add_delay():
    """Adds a 1-second delay
    
    """
    import time
    print("Start time:" + str(time.time()))
    time.sleep(1)
    print("End time:" + str(time.time()))


def iterative_delay(num_repeats):
    """Calls repeatedly the add_delay method

    :param num_repeats: number of invocations to add_delay
    """
    for i in range(num_repeats):
        print("Start iteration:" + str(i))
        add_delay()

@task()
def main_agents(repeats):
    """Different function that has the same behavior as the main

    :param repeats: number of invocations to add_delay
    """
    repeats = int(repeats)
    
    print("Executing main_agents function")
    iterative_delay(repeats)


def main():
    """Main method of the sample application."""
    
    # Get command line arguments
    repeats = int(sys.argv[1])
    
    print("Executing main function")
    iterative_delay(repeats)


if __name__ == "__main__":
    main()
