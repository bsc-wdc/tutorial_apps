package Matrixmean;

import es.bsc.compss.types.annotations.Parameter;
import es.bsc.compss.types.annotations.parameter.Direction;
import es.bsc.compss.types.annotations.parameter.Type;
import es.bsc.compss.types.annotations.task.Method;

public interface MatrixmeanItf {
	@Method(declaringClass = "Matrixmean.MatrixmeanImpl")
	void initializeRaw(
			@Parameter (direction=Direction.IN) int size); 
	
	@Method(declaringClass = "Matrixmean.MatrixmeanImpl")
	Double computeMean(
			@Parameter double[] a, 
			@Parameter(type=Type.FILE, direction=Direction.INOUT) String fileName);

	@Method(declaringClass = "Matrixmean.MatrixmeanImpl")
	void computeSquare(
			@Parameter (type=Type.FILE, direction=Direction.INOUT) String fileName, 
			@Parameter Double mean);
}
