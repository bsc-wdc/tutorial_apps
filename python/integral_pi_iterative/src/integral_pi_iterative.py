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

from pycompss.api.api import compss_wait_on
from pycompss.api.task import task


@task(returns=float)
def calculate_area(i, num_steps, number_of_batches, step_size):
    """Calculate the area below the curve within the range of parameters.

    :param i: Number of the batch to calculate.
    :param num_steps: Number of steps to divide the range.
    :param number_of_batches: Total number of batches.
    :param step_size: Size of each step.
    :return: The calculated area within the given range.
    """
    partial_area_sum = 0
    for j in range(i, num_steps, number_of_batches):
        x = (j + 0.5) * step_size
        partial_area_sum += 4 / (1 + x**2)
    return partial_area_sum


@task(returns=float)
def sum_areas(partial_area: float, total_area: float) -> float:
    """Accumulates partial area with total area.

    :param partial_area: Partial area to add.
    :param total_area: Total area where to add.
    :return: The sum of partial area and total area.
    """
    total_area += partial_area
    return total_area


def main():
    """Integral PI iterative main method."""
    # Get parameters
    num_steps = int(sys.argv[1])
    number_of_batches = int(sys.argv[2])
    step_size = 1 / num_steps

    # Run the algorithm
    total_area = 0
    for i in range(number_of_batches):
        partial_area = calculate_area(i, num_steps, number_of_batches, step_size)
        total_area = sum_areas(partial_area, total_area)

    # Wait for all tasks to finish and gather the result
    total_area = compss_wait_on(total_area)

    # Calculate PI
    pi = step_size * total_area

    # Display the result
    print("PI:", pi, "Error:", abs(np.pi - pi))


if __name__ == "__main__":
    main()
