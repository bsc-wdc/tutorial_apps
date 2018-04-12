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
package increment;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class Increment {
	
	private static final String fileName1 = "file1";
	private static final String fileName2 = "file2";
	private static final String fileName3 = "file3";
	
	
	private static void usage() {
		System.out.println("    Usage: increment.Increment <numIterations> <counterValue1> <counterValue2> <counterValue3>");
	}
	
	public static void main(String[] args) throws Exception {
		// Check and get parameters
		if (args.length != 4) {
			usage();
			throw new Exception("[ERROR] Incorrect number of parameters");
		}
		int N = Integer.parseInt(args[0]);
		int counter1 = Integer.parseInt(args[1]);
		int counter2 = Integer.parseInt(args[2]);
		int counter3 = Integer.parseInt(args[3]);
		
		// Initialize counter files
		System.out.println("Initial counter values:");
		initializeCounters(counter1, counter2, counter3);
		
		// Print initial counters state
		printCounterValues();

		// Execute increment tasks
		for (int i = 0; i < N; ++i) {
			IncrementImpl.increment(fileName1);
			IncrementImpl.increment(fileName2);
			IncrementImpl.increment(fileName3);
		}

		// Print final counters state (sync)
		System.out.println("Final counter values:");
		printCounterValues();
	}
	
	public static void initializeCounters(int counter1, int counter2, int counter3) throws Exception {
		FileOutputStream fos = null;
		// Counter1
		try {
			fos = new FileOutputStream(fileName1);
			fos.write(counter1);
			fos.close();		
		} catch (IOException e) {
			throw new Exception("[ERROR] Error initializing counter1 file", e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					throw new Exception ("[ERROR] Error closing counter2 file", e);
				}
			}
		}
		// Counter2
		try {
			fos = new FileOutputStream(fileName2);
			fos.write(counter2);
			fos.close();		
		} catch (IOException e) {
			throw new Exception ("[ERROR] Error initializing counter2 file", e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					throw new Exception ("[ERROR] Error closing counter2 file", e);
				}
			}
		}
		// Counter3
		try {
			fos = new FileOutputStream(fileName3);
			fos.write(counter3);
			fos.close();		
		} catch (IOException e) {
			throw new Exception ("[ERROR] Error initializing counter3 file", e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					throw new Exception ("[ERROR] Error closing counter3 file", e);
				}
			}
		}
	}
	
	public static void printCounterValues() throws Exception {
		int counter1 = -1;
		int counter2 = -1;
		int counter3 = -1;
		
		FileInputStream fis = null;
		// Counter1
		try {
			fis = new FileInputStream(fileName1);
			counter1 = fis.read();
		} catch (IOException e) {
			throw new Exception ("[ERROR] Error reading counter1 file", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					throw new Exception ("[ERROR] Error closing counter1 file", e);
				}
			}
		}
		// Counter2
		try {
			fis = new FileInputStream(fileName2);
			counter2 = fis.read();
		} catch (IOException e) {
			throw new Exception ("[ERROR] Error reading counter2 file", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					throw new Exception ("[ERROR] Error closing counter2 file", e);
				}
			}
		}
		// Counter3
		try {
			fis = new FileInputStream(fileName3);
			counter3 = fis.read();
		} catch (IOException e) {
			throw new Exception ("[ERROR] Error reading counter3 file", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					throw new Exception ("[ERROR] Error closing counter3 file", e);
				}
			}
		}
		
		// Print values
		System.out.println("- Counter1 value is " + counter1);
		System.out.println("- Counter2 value is " + counter2);
		System.out.println("- Counter3 value is " + counter3);
	}
	
} 
