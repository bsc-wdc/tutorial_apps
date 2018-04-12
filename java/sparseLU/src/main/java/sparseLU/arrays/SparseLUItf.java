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
package sparseLU.arrays;

import es.bsc.compss.types.annotations.Parameter;
import es.bsc.compss.types.annotations.parameter.Direction;
import es.bsc.compss.types.annotations.task.Method;


public interface SparseLUItf {

    @Method(declaringClass = "sparseLU.arrays.SparseLUImpl")
    void lu0(
        @Parameter(direction = Direction.INOUT) double[] diag
    );

    @Method(declaringClass = "sparseLU.arrays.SparseLUImpl")
    void bdiv(
	    @Parameter double[] diag,
	    @Parameter(direction = Direction.INOUT) double[] row
    );

    @Method(declaringClass = "sparseLU.arrays.SparseLUImpl")
    void bmod(
	    @Parameter double[] row,
	    @Parameter double[] col,
	    @Parameter(direction = Direction.INOUT) double[] inner
    );

    @Method(declaringClass = "sparseLU.arrays.SparseLUImpl")
    void fwd(
		@Parameter double[] diag,
        @Parameter(direction = Direction.INOUT) double[] col
    );

    @Method(declaringClass = "sparseLU.arrays.SparseLUImpl")
    double[] bmodAlloc(
    	@Parameter double[] row,
    	@Parameter double[] col
     );
    
    @Method(declaringClass = "sparseLU.arrays.SparseLUImpl")
    double[] initBlock(
    	@Parameter int i,
    	@Parameter int j,
    	@Parameter int N,
    	@Parameter int M
	);

}
