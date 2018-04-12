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

import java.io.FileOutputStream;
import java.io.IOException;


public class Matmul {
	private static int MSIZE;
	private static int BSIZE;

	private static double [][][] A;
	private static double [][][] B;
	private static double [][][] C;
	
	
	private static void usage() {
		System.out.println("    Usage: matmul.arrays.Matmul <MSize> <BSize>");
	}
	
	public static void main(String[] args) throws Exception {
		// Check and get parameters
		if (args.length != 2) {
			usage();
			throw new Exception("[ERROR] Incorrect number of parameters");
		}
		MSIZE = Integer.parseInt(args[0]);
		BSIZE = Integer.parseInt(args[1]);
		
		// Initialize matrices
		System.out.println("[LOG] MSIZE parameter value = " + MSIZE);
		System.out.println("[LOG] BSIZE parameter value = " + BSIZE);
		A = initializeMatrix();
		B = initializeMatrix();
		
		// Compute matrix multiplication C = A x B
		computeMultiplication();
		
		// Uncomment the following line if you wish to see the result in the stdout
		//printMatrix(C, "C (Result)");
		// Uncomment the following line if you wish to store the result in a file
		//storeMatrix("c_result.txt");
		
		// End
		System.out.println("[LOG] Main program finished.");
	}
	
	private static double[][][] initializeMatrix() {
		double[][][] matrix = new double[MSIZE][MSIZE][BSIZE*BSIZE];
		for (int i = 0; i < MSIZE; ++i) {
			for (int j = 0; j < MSIZE; ++j) {
				matrix[i][j] = MatmulImpl.initializeBlock(BSIZE);
			}
		}
		
		return matrix;
	}
	
	private static void computeMultiplication() {
		// Allocate result matrix C
		System.out.println("[LOG] Allocating C matrix space");
		C = new double[MSIZE][MSIZE][BSIZE*BSIZE];
		
		// Compute result
		System.out.println("[LOG] Computing Result");
		for (int i = 0; i < MSIZE; i++) {
			for (int j = 0; j < MSIZE; j++) {
				for (int k = 0; k < MSIZE; k++) {
					MatmulImpl.multiplyAccumulative(A[i][k], B[k][j], C[i][j]);
				}
            }
		}
	}
		
	@SuppressWarnings("unused")
	private static void storeMatrix(String fileName) {
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			for (int i = 0; i < MSIZE; ++i) {
				for (int j = 0; j < MSIZE; ++j) {
					for (int block = 0; block < BSIZE*BSIZE; ++block) {
						String value = String.valueOf(C[i][j][block]) + " ";
						fos.write(value.getBytes());
					}
					fos.write("\n".getBytes());
				}
				fos.write("\n".getBytes());
			}
			fos.close();
    	} catch(IOException ioe) {
			ioe.printStackTrace();
			System.exit(-1);
    	}
	}

	@SuppressWarnings("unused")
	private static void printMatrix(double[][][] matrix, String name) {
		System.out.println("MATRIX " + name);
		for (int i = 0; i < MSIZE; i++) {
			 for (int j = 0; j < MSIZE; j++) {
				printBlock(matrix[i][j]);
			 }
			 System.out.println("");
		 }
	}
	
	private static void printBlock(double[] block) {
		for (int k = 0; k < block.length; k++) {
			 System.out.print(block[k] + " ");
		 }
		System.out.println("");
	}
	
}

