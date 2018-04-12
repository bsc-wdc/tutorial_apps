package wordcount.multipleFiles;

import java.util.HashMap;

import es.bsc.compss.types.annotations.Parameter;
import es.bsc.compss.types.annotations.parameter.Direction;
import es.bsc.compss.types.annotations.parameter.Type;
import es.bsc.compss.types.annotations.task.Method;


public interface WordcountItf {
	
	@Method(declaringClass = "wordcount.multipleFiles.Wordcount")
	public HashMap<String, Integer> mergeResults(
			@Parameter HashMap<String, Integer> m1, 
			@Parameter HashMap<String, Integer> m2
	);
	
	@Method(declaringClass = "wordcount.multipleFiles.Wordcount")
	public HashMap<String, Integer> wordCount(
			@Parameter(type = Type.FILE, direction = Direction.IN)String filePath
	);

}
