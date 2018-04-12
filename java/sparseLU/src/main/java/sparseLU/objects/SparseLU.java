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
package sparseLU.objects;

import java.io.FileOutputStream;
import java.io.IOException;


public class SparseLU {

    private static int MSIZE; // N = matrix size
    private static int BSIZE; // M = block size

    private static Block[][] A;


    private static void usage() {
        System.out.println("    Usage: sparseLU.objects.SparseLU <MSize> <BSize>");
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
        generateMatrix();

        // Computing Sparse LU algorithm
        computeSparseLU();

        // Uncomment the following line if you wish to see the result in the stdout
        // printMatrix(AfileNames, "Result");
        // Uncomment the following line if you wish to store the result in a file
        // storeMatrix("c_result.txt");

        // End
        System.out.println("[LOG] Main program finished.");
    }

    private static void generateMatrix() {
        A = new Block[MSIZE][MSIZE];
        for (int i = 0; i < MSIZE; i++) {
            for (int j = 0; j < MSIZE; j++) {
                A[i][j] = new Block(MSIZE, BSIZE, i, j);
            }
        }
    }

    private static void computeSparseLU() {
        System.out.println("[LOG] Computing SparseLU algorithm on A");
        for (int kk = 0; kk < MSIZE; kk++) {
            A[kk][kk].lu0();
            for (int jj = kk + 1; jj < MSIZE; jj++) {
                if (A[kk][jj] != null) {
                    A[kk][jj].fwd(A[kk][kk]);
                }
            }
            for (int ii = kk + 1; ii < MSIZE; ii++) {
                if (A[ii][kk] != null) {
                    A[ii][kk].bdiv(A[kk][kk]);
                    for (int jj = kk + 1; jj < MSIZE; jj++) {
                        if (A[kk][jj] != null) {
                            if (A[ii][jj] == null) {
                                A[ii][jj] = Block.bmodAlloc(A[ii][kk], A[kk][jj]);
                            } else {
                                A[ii][jj].bmod(A[ii][kk], A[kk][jj]);
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
                    if (A[i][j] == null) {
                        fos.write("null\n".getBytes());
                    } else {
                        String block = A[i][j].toString() + "\n";
                    }
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
                    throw new Exception("[ERROR] Error closing result matrix", e);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private static void printMatrix(Block[][] matrix, String name) {
        System.out.println("MATRIX " + name + ":");
        for (int i = 0; i < MSIZE; i++) {
            for (int j = 0; j < MSIZE; j++) {
                if (matrix[i][j] == null) {
                    System.out.println("null");
                } else {
                    matrix[i][j].printBlock();
                }
            }
        }
    }

}
