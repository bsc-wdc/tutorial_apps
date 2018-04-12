package wordcount.uniqueFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;


public class Wordcount {
	private static String DATA_FILE;
	private static int BLOCK_SIZE;
	private static int NUM_BLOCKS;
	private static BufferedReader buffer;
	
	public static void main(String args[]) {
		// Get parameters
		if (args.length != 2) {
			System.out.println("[ERROR] Usage: Wordcount <DATA_FILE> <BLOCK_SIZE>");
			System.exit(-1);
		}
		
		try {
			// Initialize file Names
			initializeVariables(args);
		
			// Run wordcount app
			computeWordCount();
		
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: Cannot retrieve values from buffer");
			e.printStackTrace();
		} finally {
			if (buffer!=null){
				try {
					buffer.close();
				} catch (IOException e) {
					//Nothing to do
				}
			}
		}
	}
	
	private static void computeWordCount() {
		
		System.out.println("[LOG] Computing word count result");		
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		
		
		//TODO: Add here the main code to calculate the parallel word count
		//
		// Hint: You do not need to implement the counting and merge algorithms we have already provided it. 
		// Use the helper methods wordCountBlock and mergeResults to do this code
		//
		// Variables are already initialized:
			// DATA_FILE stores the file to count the words; 
			// BLOCK SIZE stores the size of the document block;
			// NUM_BLOCKS stores the calculated number of blocks;
			// resutls is a hash map to store the words the number of times appeared in the text   
		
		
		System.out.println("[LOG] Counted Words is : " + result.keySet().size());
		
	}
	
	//Initialize the variables and calculates de number of blocks
	private static void initializeVariables (String args[]) throws FileNotFoundException {
		//Get data file
		DATA_FILE = args[0];
		BLOCK_SIZE = Integer.valueOf(args[1]);
		System.out.println("DATA_FILE parameter value = " + DATA_FILE);
		System.out.println("BLOCK_SIZE parameter value = " + BLOCK_SIZE);
		double fileLength = (double)(new File(DATA_FILE).length());
		NUM_BLOCKS = (int) Math.ceil(fileLength/((double)(BLOCK_SIZE)));
		buffer = new BufferedReader(new FileReader(new File(DATA_FILE)));
		
	}
	
	// Merge the words counted in two block
	public static HashMap<String, Integer> mergeResults(HashMap<String, Integer> m1, HashMap<String, Integer> m2) {
		m2.putAll(m1);
		return m2;
	}
	
	
	
	//Counts the words in Block where we pass a file
	public static HashMap<String, Integer> wordCountBlock(String filePath, int start, int bsize) {
		File file = new File(filePath);
		HashMap<String, Integer> res = new HashMap<String, Integer>();
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			br.skip(start);
			
			long readedBytes = 0;
			String line;
			while ((line = br.readLine()) != null && readedBytes < bsize) {
				//readedBytes += line.getBytes().length;
				String[] words = line.split(" +");
				for (String word : words) {
					if (!word.isEmpty()&& !word.equalsIgnoreCase("\n")){
						if (res.containsKey(word)) {
							res.put(word, res.get(word) + 1);
						} else {
							res.put(word, 1);
						}
						readedBytes += word.getBytes().length;
						if (readedBytes >= bsize) {
							break;
						}
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
		System.out.println("Words counted: "+ res.size());
		return res;
	}
	

}
