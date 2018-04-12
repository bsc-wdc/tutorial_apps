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
package sparseLU.objects;

import es.bsc.compss.types.annotations.Parameter;
import es.bsc.compss.types.annotations.task.Method;


public interface SparseLUItf {

	@Method(declaringClass = "sparseLU.objects.Block")
    void lu0();

    @Method(declaringClass = "sparseLU.objects.Block")
    void bdiv(
	    @Parameter Block diag
    );

    @Method(declaringClass = "sparseLU.objects.Block")
    void bmod(
	    @Parameter Block row,
	    @Parameter Block col
    );

    @Method(declaringClass = "sparseLU.objects.Block")
    void fwd(
		@Parameter Block diag
    );

    @Method(declaringClass = "sparseLU.objects.Block")
    Block bmodAlloc(
    	@Parameter Block row,
    	@Parameter Block col
     );
    
    @Method(declaringClass = "sparseLU.objects.Block")
    Block initBlock(
    	@Parameter int i,
    	@Parameter int j,
    	@Parameter int N,
    	@Parameter int M
	);
	
}
