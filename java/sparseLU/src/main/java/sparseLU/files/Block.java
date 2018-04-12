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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


public class Block {

    private final int blockSize;
    private final double[][] data;


    public Block(int i, int j, int msize, int bsize) {
        this.blockSize = bsize;
        this.data = initBlock(i, j, msize, this.blockSize);
    }

    public Block(String filename, int bsize) {
        this.blockSize = bsize;
        this.data = new double[this.blockSize][this.blockSize];

        try (FileReader filereader = new FileReader(filename); BufferedReader br = new BufferedReader(filereader);) {
            StringTokenizer tokens;
            String nextLine;
            for (int i = 0; i < this.blockSize; i++) {
                nextLine = br.readLine();
                tokens = new StringTokenizer(nextLine);
                for (int j = 0; j < this.blockSize && tokens.hasMoreTokens(); j++) {
                    this.data[i][j] = Double.parseDouble(tokens.nextToken());
                }
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static double[][] initBlock(int ii, int jj, int N, int M) {
        double[][] block = new double[M][M];
        int initVal = 1325;
        for (int k = 0; k < N; k++) {
            for (int l = 0; l < N; l++) {
                if (!isNull(k, l)) {
                    for (int i = 0; i < M; i++) {
                        for (int j = 0; j < M; j++) {
                            initVal = (3125 * initVal) % 65536;
                            if (k == ii && l == jj) {
                                block[i][j] = ((initVal - 32768.0) / 16384.0);
                            }
                        }
                    }
                }
            }
        }
        return block;
    }

    private static boolean isNull(int ii, int jj) {
        boolean nullEntry = false;
        if ((ii < jj) && (ii % 3 != 0)) {
            nullEntry = true;
        }
        if ((ii > jj) && (jj % 3 != 0)) {
            nullEntry = true;
        }
        if (ii % 2 == 1) {
            nullEntry = true;
        }
        if (jj % 2 == 1) {
            nullEntry = true;
        }
        if (ii == jj) {
            nullEntry = false;
        }
        if (ii == jj - 1) {
            nullEntry = false;
        }
        if (ii - 1 == jj) {
            nullEntry = false;
        }

        return nullEntry;
    }

    protected void printBlock() {
        for (int i = 0; i < this.blockSize; i++) {
            for (int j = 0; j < this.blockSize; j++) {
                System.out.print(this.data[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void blockToDisk(String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            for (int i = 0; i < this.blockSize; i++) {
                for (int j = 0; j < this.blockSize; j++) {
                    String str = (new Double(this.data[i][j])).toString() + " ";
                    fos.write(str.getBytes());
                }
                fos.write("\n".getBytes());
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void lu0() {
        for (int k = 0; k < this.blockSize; k++) {
            for (int i = k + 1; i < this.blockSize; i++) {
                this.data[i][k] = this.data[i][k] / this.data[k][k];
                for (int j = k + 1; j < blockSize; j++) {
                    this.data[i][j] -= this.data[i][k] * this.data[k][j];
                }
            }
        }
    }

    public void bdiv(Block diag) {
        for (int i = 0; i < this.blockSize; i++) {
            for (int k = 0; k < this.blockSize; k++) {
                this.data[i][k] = this.data[i][k] / diag.data[k][k];
                for (int j = k + 1; j < blockSize; j++) {
                    this.data[i][j] -= this.data[i][k] * diag.data[k][j];
                }
            }
        }
    }

    public void bmod(Block row, Block col) {
        for (int i = 0; i < this.blockSize; i++) {
            for (int j = 0; j < this.blockSize; j++) {
                for (int k = 0; k < this.blockSize; k++) {
                    this.data[i][j] -= row.data[i][k] * col.data[k][j];
                }
            }
        }
    }

    public void fwd(Block diag) {
        for (int j = 0; j < this.blockSize; j++) {
            for (int k = 0; k < this.blockSize; k++) {
                for (int i = k + 1; i < this.blockSize; i++) {
                    this.data[i][j] -= diag.data[i][k] * this.data[k][j];
                }
            }
        }
    }

}
