/*
 *  Copyright 2002-2015 Barcelona Supercomputing Center (www.bsc.es)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package matmul.arrays;


public class MatmulImpl {
	
	public static void multiplyAccumulative(double[] a, double[] b, double[] c) {
		int M = (int)Math.sqrt(a.length);
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < M; j++) {
				for (int k = 0; k < M; k++) {
					c[i*M + j] += a[i*M + k] * b[k*M + j];
				}
			}
		}
	}
	
	public static double[] initializeBlock(int size) {
		double[] block = new double[size*size];
		for (int i = 0; i < size*size; ++i) {
			block[i] = (double)(Math.random()*10.0);
		}
		
		return block;
	}
	
}
