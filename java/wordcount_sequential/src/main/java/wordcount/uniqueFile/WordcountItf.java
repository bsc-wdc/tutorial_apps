package wordcount.uniqueFile;

import java.util.HashMap;



public interface WordcountItf {
		
	//TODO Add Annotations to tasks
	
	public HashMap<String, Integer> mergeResults(
			HashMap<String, Integer> m1, 
			HashMap<String, Integer> m2
	);
	

	HashMap<String, Integer> wordCountBlock(
			String filePath, 
			int start, 
			int bsize
	);
	
	
}
