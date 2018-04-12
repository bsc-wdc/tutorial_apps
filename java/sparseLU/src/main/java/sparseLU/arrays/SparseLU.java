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
package sparseLU.arrays;

import java.io.FileOutputStream;
import java.io.IOException;


public class SparseLU {

    public static int MSIZE; // Matrix size
    public static int BSIZE; // Block size

    private static double[][][] A;


    private static void usage() {
        System.out.println("    Usage: sparseLU.arrays.SparseLU <MSize> <BSize>");
    }

    public static void main(String args[]) throws Exception {
        // Check and get parameters
        if (args.length != 2) {
            usage();
            throw new Exception("[ERROR] Incorrect number of parameters");
        }
        MSIZE = Integer.valueOf(args[0]);
        BSIZE = Integer.valueOf(args[1]);
        System.out.println("[LOG] Running with the following parameters:");
        System.out.println("[LOG]  - Matrix Size: " + MSIZE);
        System.out.println("[LOG]  - Block Size:  " + BSIZE);

        // Initializing A values
        System.out.println("[LOG] Initializing Matrix");
        A = new double[MSIZE][MSIZE][BSIZE * BSIZE];
        A = generateMatrix();

        // Computing Sparse LU algorithm
        computeSparseLU();

        // Uncomment the following line if you wish to see the result in the stdout
        // printMatrix(A, "Result");
        // Uncomment the following line if you wish to store the result in a file
        // storeMatrix("c_result.txt");

        // End
        System.out.println("[LOG] Main program finished.");
    }

    private static double[][][] generateMatrix() {
        double[][][] matrix = new double[MSIZE][MSIZE][BSIZE * BSIZE];
        for (int i = 0; i < MSIZE; ++i) {
            for (int j = 0; j < MSIZE; ++j) {
                matrix[i][j] = SparseLUImpl.initBlock(i, j, MSIZE, BSIZE);
            }
        }

        return matrix;
    }

    private static void computeSparseLU() {
        System.out.println("[LOG] Computing SparseLU algorithm on A");
        for (int k = 0; k < MSIZE; k++) {
            SparseLUImpl.lu0(A[k][k]);
            for (int j = k + 1; j < MSIZE; j++) {
                if (A[k][j] != null) {
                    SparseLUImpl.fwd(A[k][k], A[k][j]);
                }
            }
            for (int i = k + 1; i < MSIZE; i++) {
                if (A[i][k] != null) {
                    SparseLUImpl.bdiv(A[k][k], A[i][k]);
                    for (int j = k + 1; j < MSIZE; j++) {
                        if (A[k][j] != null) {
                            if (A[i][j] == null) {
                                A[i][j] = SparseLUImpl.bmodAlloc(A[i][k], A[k][j]);
                            } else {
                                SparseLUImpl.bmod(A[i][k], A[k][j], A[i][j]);
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private static void storeMatrix(String fileName) throws Exception {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            for (int i = 0; i < MSIZE; ++i) {
                for (int j = 0; j < MSIZE; ++j) {
                    for (int k = 0; k < BSIZE * BSIZE; ++k) {
                        String value = String.valueOf(A[i][j][k]) + " ";
                        fos.write(value.getBytes());
                    }
                    fos.write("\n".getBytes());
                }
                fos.write("\n".getBytes());
            }
        } catch (IOException ioe) {
            throw new Exception("[ERROR] Error storing result matrix", ioe);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    throw new Exception("[ERROR] Error storing result matrix", e);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private static void printMatrix(double[][][] matrix, String name) {
        System.out.println("MATRIX " + name + ":");
        for (int i = 0; i < MSIZE; i++) {
            for (int j = 0; j < MSIZE; j++) {
                SparseLUImpl.printBlock(matrix[i][j], BSIZE);
            }
            System.out.println("");
        }
    }

}
