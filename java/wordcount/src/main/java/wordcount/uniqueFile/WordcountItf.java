package wordcount.uniqueFile;

import java.util.HashMap;

import es.bsc.compss.types.annotations.Parameter;
import es.bsc.compss.types.annotations.parameter.Direction;
import es.bsc.compss.types.annotations.parameter.Type;
import es.bsc.compss.types.annotations.task.Method;


public interface WordcountItf {
		
	@Method(declaringClass = "wordcount.uniqueFile.Wordcount")
	public HashMap<String, Integer> mergeResults(
			@Parameter HashMap<String, Integer> m1, 
			@Parameter HashMap<String, Integer> m2
	);
	
	@Method(declaringClass = "wordcount.uniqueFile.Wordcount")
	HashMap<String, Integer> wordCountBlock(
			@Parameter(type=Type.FILE, direction=Direction.IN) String filePath, 
			@Parameter int start, 
			@Parameter int bsize
	);
	
}
