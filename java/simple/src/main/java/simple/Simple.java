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
package simple;

import java.io.FileInputStream;
import java.io.FileOutputStream;


public class Simple {
	
	private static final String fileName = "counter";
	
	private static void usage() {
		System.out.println("    Usage: simple <counterValue>");
	}
	
	public static void main(String[] args) throws Exception {
		// Check and get parameters
		if (args.length != 1) {
			usage();
			throw new Exception("[ERROR] Incorrect number of parameters");
		}
		int initialValue = Integer.parseInt(args[0]);

		// Write value
		FileOutputStream fos = new FileOutputStream(fileName);
		fos.write(initialValue);
		fos.close();
		System.out.println("Initial counter value is " + initialValue);

		//Execute increment
		SimpleImpl.increment(fileName);

		// Write new value
		FileInputStream fis = new FileInputStream(fileName);
		int finalValue = fis.read();
		fis.close();
		System.out.println("Final counter value is " + finalValue);
	}
	
} 
