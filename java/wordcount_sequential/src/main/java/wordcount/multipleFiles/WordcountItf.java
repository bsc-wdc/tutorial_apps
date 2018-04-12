package wordcount.multipleFiles;

import java.util.HashMap;


public interface WordcountItf {
	
	//TODO Add Annotations to tasks
	public HashMap<String, Integer> mergeResults(
			HashMap<String, Integer> m1, 
			HashMap<String, Integer> m2
	);
	
	public HashMap<String, Integer> wordCount(
			String filePath
	);

}
