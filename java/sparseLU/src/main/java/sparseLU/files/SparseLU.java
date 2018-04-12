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
package sparseLU.files;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


public class SparseLU {

    public static int MSIZE; // Matrix size
    public static int BSIZE; // Block size

    private static String[][] AfileNames;
    private static boolean[][] empty_blocks;


    private static void usage() {
        System.out.println("    Usage: sparseLU.files.SparseLU <MSize> <BSize>");
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
        initializeVariables();
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

    private static void initializeVariables() {
        // File names
        AfileNames = new String[MSIZE][MSIZE];
        for (int i = 0; i < MSIZE; ++i) {
            for (int j = 0; j < MSIZE; ++j) {
                AfileNames[i][j] = "A." + i + "." + j;
            }
        }

        // Boolean matrix
        empty_blocks = new boolean[MSIZE][MSIZE];
    }

    private static void generateMatrix() {
        for (int i = 0; i < MSIZE; ++i) {
            for (int j = 0; j < MSIZE; ++j) {
                empty_blocks[i][j] = SparseLUImpl.initBlock(AfileNames[i][j], i, j, MSIZE, BSIZE);
            }
        }
    }

    public static void computeSparseLU() {
        System.out.println("[LOG] Computing SparseLU algorithm on A");
        String file, file1;
        for (int k = 0; k < MSIZE; k++) {
            file = AfileNames[k][k];
            SparseLUImpl.lu0(file, BSIZE);
            for (int j = k + 1; j < MSIZE; j++) {
                if (!empty_blocks[k][j]) {
                    file1 = AfileNames[k][j];
                    SparseLUImpl.fwd(file, file1, BSIZE);
                }
            }
            for (int i = k + 1; i < MSIZE; i++) {
                if (!empty_blocks[i][k]) {
                    String file2 = AfileNames[i][k];
                    SparseLUImpl.bdiv(file, file2, BSIZE);
                    for (int j = k + 1; j < MSIZE; j++) {
                        if (!empty_blocks[k][j] && !empty_blocks[i][j]) {
                            String file3 = AfileNames[k][j];
                            String file4 = AfileNames[i][j];
                            if (empty_blocks[i][j]) {
                                createEmptyBlock(file4);
                                empty_blocks[i][j] = false;
                            }
                            SparseLUImpl.bmod(file2, file3, file4, BSIZE);
                        }
                    }
                }
            }
        }
    }

    private static void createEmptyBlock(String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write("".getBytes());
        } catch (IOException ioe) {
            System.err.println("Error creating empty block");
            ioe.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private static void storeMatrix(String fileName) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            for (int i = 0; i < MSIZE; ++i) {
                for (int j = 0; j < MSIZE; ++j) {
                    if (empty_blocks[i][j]) {
                        fos.write("null\n".getBytes());
                    } else {
                        String blockName = AfileNames[i][j];
                        FileReader filereader = new FileReader(blockName);
                        BufferedReader br = new BufferedReader(filereader);
                        StringTokenizer tokens;
                        String nextLine;
                        for (int iblock = 0; iblock < BSIZE; ++iblock) {
                            nextLine = br.readLine();
                            tokens = new StringTokenizer(nextLine);
                            for (int jblock = 0; jblock < BSIZE && tokens.hasMoreTokens(); ++jblock) {
                                String value = tokens.nextToken() + " ";
                                fos.write(value.getBytes());
                            }
                        }
                        br.close();
                        filereader.close();
                        fos.write("\n".getBytes());
                    }
                }
                fos.write("\n".getBytes());
            }

        } catch (IOException ioe) {
            throw new Exception("[ERROR] Error storing result matrix", ioe);
        }
    }

    @SuppressWarnings("unused")
    private static void printMatrix(String[][] fileNames, String name) throws Exception {
        System.out.println("MATRIX " + name);
        for (int i = 0; i < MSIZE; i++) {
            for (int j = 0; j < MSIZE; j++) {
                if (empty_blocks[i][j]) {
                    System.out.println("null");
                } else {
                    Block b = new Block(fileNames[i][j], BSIZE);
                    b.printBlock();
                }
            }
            System.out.println("");
        }
    }
}
