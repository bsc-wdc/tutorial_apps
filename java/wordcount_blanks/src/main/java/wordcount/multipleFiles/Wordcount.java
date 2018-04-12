package wordcount.multipleFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;


public class Wordcount {
	private static String DATA_FOLDER;
	
	private static String[] filePaths;
	private static HashMap<String, Integer> result;
	
	public static void main(String args[]) {
		// Get parameters
		if (args.length != 1) {
			System.out.println("[ERROR] Usage: Wordcount <DATA_FOLDER>");
			System.exit(-1);
		}
		// Initialize file Names
		initializeVariables(args);
		// Run wordcount app
		computeWordCount();
	
	}
	
	private static void computeWordCount() {

		// Compute result
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		System.out.println("[LOG] Computing result");
		for (int i = 0; i < filePaths.length; ++i) {
			HashMap<String, Integer> partialResult = wordCount(filePaths[i]);
			result = mergeResults(partialResult, result);
		}
		System.out.println("[LOG] Result size = " + result.keySet().size());
				
	}

	private static void initializeVariables (String[] args) {
		DATA_FOLDER = args[0];
		System.out.println("DATA_FOLDER parameter value = " + DATA_FOLDER);
		int numFiles = new File(DATA_FOLDER).listFiles().length;
		filePaths = new String[numFiles];
		int i = 0;
		for (File f : new File(DATA_FOLDER).listFiles()) {
			filePaths[i] = f.getAbsolutePath();
			i = i + 1;
		}
	}
	
	public static HashMap<String, Integer> mergeResults(HashMap<String, Integer> m1, HashMap<String, Integer> m2) {
		m2.putAll(m1);
		return m2;
	}
	
	public static HashMap<String, Integer> wordCount(String filePath) {
		File file = new File(filePath);
		HashMap<String, Integer> res = new HashMap<String, Integer>();
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				String[] words = line.split(" ");
				for (String word : words) {
					if (res.containsKey(word)) {
						res.put(word, res.get(word) + 1);
					} else {
						res.put(word, 1);
					}
				}
			}
		} catch (Exception e) {
			System.err.println("ERROR: Cannot retrieve values from " + file.getName());
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					System.err.println("ERROR: Cannot close buffered reader on file " + file.getName());
					e.printStackTrace();
				}
			}
			if (fr != null) {
				try {
					fr.close();
				} catch (Exception e) {
					System.err.println("ERROR: Cannot close file reader on file " + file.getName());
					e.printStackTrace();
				}
			}
		}
		
		return res;
	}

}
