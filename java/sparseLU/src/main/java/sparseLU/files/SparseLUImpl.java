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

public class SparseLUImpl {

    public static boolean initBlock(String fileName, int i, int j, int msize, int bsize) {
        if (isNull(i, j)) {
            return true;
        } else {
            Block a = new Block(i, j, msize, bsize);
            a.blockToDisk(fileName);
            return false;
        }
    }

    public static void lu0(String diag, int bsize) {
        Block a = new Block(diag, bsize);
        a.lu0();
        a.blockToDisk(diag);
    }

    public static void bdiv(String diag, String row, int bsize) {
        Block a = new Block(diag, bsize);
        Block b = new Block(row, bsize);
        b.bdiv(a);
        b.blockToDisk(row);
    }

    public static void bmod(String row, String col, String inner, int bsize) {
        Block a = new Block(row, bsize);
        Block b = new Block(col, bsize);
        Block c = new Block(inner, bsize);

        c.bmod(a, b);
        c.blockToDisk(inner);
    }

    public static void fwd(String diag, String col, int bsize) {
        Block a = new Block(diag, bsize);
        Block b = new Block(col, bsize);
        b.fwd(a);
        b.blockToDisk(col);
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

}
