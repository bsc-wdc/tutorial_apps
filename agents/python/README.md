# COMPSs AGENTS User Interactions
This folder contains all the necessary material to follow a tutorial to learn how to use COMPSs Agents. For doing so, this tutorial deploys two agents on the local node and submits invocations its REST API.

## Applications
For that we will use two simple Python applications. The `test_app` application has one single method (`main`) which accepts no parameters and just waits for 5 seconds printing the start and finish timestamps. 

The second application `demo_app` is a simple application demonstrating how a COMPSs application has to be edited to enable its execution with agents. The original pyCOMPSs application creates a set of tasks that add a 1-second delay. The application accepts 1 single parameter that indicates the number of tasks to be created. Each of these tasks waits for 1 second and prints its starting and ending timestamps. For doing that, the `main` method parses the command-line argument and invokes the `iterative_delay` method. This latter method invokes as many times as indicated in the argument the `add_delay` method.

In order to enable the exection of the `demo_app` in a COMPSs Agent deployment, an additional method has been added: `main_agents`. This method -- decorated with @task -- does exactly the same as the `main` method but using method parameters instead of obtaining the command-line arguments.

The code of both applications can be found in the `app` folder in this repostory.

## Execution steps
### 1. Starting the Agent
The following command starts the agent offering its REST API on port 46101 and the Comm API on port 46102. The Agent will leave all the log files in the `/tmp/Agent1` folder.
``` bash
compss_agent_start \
    --hostname=127.0.0.1 \
    --pythonpath=<absolute_path_to_repo>/app/ \
    --log_dir=/tmp/Agent1 \
    --rest_port=46101 \
    --comm_port=46102 \
```

Submitting a GET method request to the REST API on COMPSs/test allows users to check that the agent has been properly started.
This can be done with the following command:
```
curl -XGET http://127.0.01:46101/COMPSs/test
```

Also, the current resource pool configuration can be queried using the REST API
```
curl -XGET http://127.0.0.1:46101/COMPSs/resources | jq
```

As explained, the `/tmp/Agent1` folder contains all the logs of the deployed agent. For this tutorial, it will be interesting to monitor the `jobs` folder.

### 2. Submitting a request to execute a function to an isolated agent
The Agent API offers an endpoint where to submit requests to execute applications; however, to simplify the invocation, the COMPSs framework provides the `compss_agents_call_operation` script.  

The following command requests an execution of `main` method of the Python module named `test_app` with no parameters.
```
compss_agent_call_operation \
   --master_node=127.0.0.1 \
   --master_port=46101 \
   --lang=PYTHON \
   test_app
```
Once the Agent receives this invocation, it should print the following message `Received REST call to run a PYTHON method` in the standard output of the Agent. After a 5 seconds (plus some overhead due to the starting all the internal agent), it will appear a message indicating how long it took for the request to run.
Since the method has been invoked as a task, it has been executed as a job (job1). Therefore, we will have a `job1_NEW.out` and a `job1_NEW.err` files in the `/tmp/Agent1/jobs` folder. There we can check that the output of the job corresponds to what should be printed by an execution of the `main` method of the `test_app`.


One of the key features of COMPSs Agents is the resource management to ensure resource exclusivity. To verify that, the previous execution request can be submitted three times in a row. The default configuration of an agent sets up a single CPU core; therefore, only one task can run at a time. The three submitted requests will enqueue and be executed subsequently. Right after the submission, the Agent output log should contain three times the acknowledge of receiving the request. After the submission and waiting for about 15 seconds, the output log should show some messages similar to this:
```
Received REST call to run a PYTHON method
Received REST call to run a PYTHON method
Received REST call to run a PYTHON method
App completed after 5042
App completed after 9606
App completed after 14158
```
Although the execution time for each application should be 5 seconds, the time-to-complete for the third invocation usually is less that 15 seconds (10 waiting + 5 executing). This is because the delay between request. The actual times when each task has been executed can be checked at the jobs folder, where there should be 4 jobs: the first execution and these three. Checking the output of jobs 2 to 4, it can be verified that the Agent has guaranteed the exclusivity of resources.

### 3. Submitting a request to execute a workflow to an isolated agent
The `main` method of the `test_app` application is a simple task; however, the `demo_app` application has methods invoking other functions decorated with `@task`. For instance, the `main_agents` method creates N `add_delay` tasks where N depends on an input parameter.

Submitting the following command will start the execution of the main method that should create 5 `add_delay` tasks
```
compss_agent_call_operation \
   --master_node=127.0.0.1 \
   --master_port=46101 \
   --method_name=main_agents \
   --lang=PYTHON \
   demo_app 5
```

In the output log of the Agent, there is no difference with the previous invocation. However, looking at the `jobs` folder, it is plain to see that the execution is different. Now, it should contain 6 new jobs: job5 which corresponds to the execution of the `main_agent` method and jobs 5 to 9 corresponding to the executions of `add_delay` tasks. The workflow has not run any task in parallel because the agent has a single CPU core configured and the nested tasks have been executed sequentially.

### 4. Changing the resource pool of the Agent
The Agent API also offers methods to modify the resource pool of the Agent. With the `compss_agent_add_resources` script, the amount of local resources can be increased. The following command enables the agent to add 4 CPU cores to its resource pool.
```
compss_agent_add_resources \
   --agent_node=127.0.0.1 \
   --agent_port=46101 \
   --cpu=4 \
   127.0.0.1
```
And the current resource configuration can be checked with the same command. After running the command, the number of CPU Cores should be increased from 1 to 5.
```
 {
      "name": "127.0.0.1",
      "description": {
        "storage_type": "[unassigned]",
        "storage_bandwidth": -1,
        "storage_size": -1,
        "memory_type": "[unassigned]",
        "processors": [
          {
            "name": "MainProcessor",
            "units": 5,
            "architecture": "[unassigned]"
          }
        ],
        "memory_size": -1
      },
      "adaptor": "es.bsc.compss.types.COMPSsMaster"
    }
  ],
  "time": 1738681173102
}
```

Now that the agent can run up to 5 simultaneous tasks, it has enough resources to host all the task from the nested work flow in `demo_app`. Submitting the same command will start the execution of the `main_agents` method again.
```
compss_agent_call_operation \
   --master_node=127.0.0.1 \
   --master_port=46101 \
   --method_name=main_agents \
   --lang=PYTHON \
   demo_app 5
```

However, the response time of the Agent this time is reduced to a bit more of a second. The timestamps printed in jobs 11 to 16 (job10 is the main_agents) show that the 5 tasks have been executed in parallel allowing the whole worfklow execution time to shrink.

```
Received REST call to run a PYTHON method
App completed after 1513
```

Likewise, the REST API offers an endpoint to reduce the amount of resources of the pool. This endpoint is easily accessible with the `compss_agents_reduce_resources`. Executing the following command reduces the number of CPU cores by 3; only 2 cpu cores remain and 2 tasks can be executed at the same time.
```
compss_agent_reduce_resources --agent_node=127.0.0.1 --agent_port=46101 --cpu=3 127.0.0.1
curl -XGET http://localhost:46101/COMPSs/resources | jq
{
  "resources": [
    {
      "name": "127.0.0.1",
      "description": {
        "storage_type": "[unassigned]",
        "storage_bandwidth": -1,
        "storage_size": -1,
        "memory_type": "[unassigned]",
        "processors": [
          {
            "name": "MainProcessor",
            "units": 2,
            "architecture": "[unassigned]"
          }
        ],
        "memory_size": -1
      },
      "adaptor": "es.bsc.compss.types.COMPSsMaster"
    }
  ],
  "time": 1738681874124
}

```

Requesting one more time the execution of the `main_agents` method, the behaviour will be similar, but, given that only 2 cpus are available, the execution should take a bit more than 3 seconds.
```
Received REST call to run a PYTHON method
App completed after 3295
```
This time, job 17 should contain the execution of the `main_agent` method and jobs 18 to 22 correspond to `add_delay` tasks.


### 5. Setting up Agents interaction
Although paralellizing the execution of task-based workflows using the local resources offers benefits to the user, the actual potential of COMPSs agents is in the interaction between agents. For demonstrating how to setup agents to interact among them, a second agent is required. It can be instantiated in the same host using the following command.
``` bash
compss_agent_start \
    --hostname=127.0.0.2 \
    --pythonpath=<absolute_path_to_repo>/app/ \
    --log_dir=/tmp/Agent2 \
    --rest_port=46201 \
    --comm_port=46202 
```

In this case, the second agent offers its REST API on port 46201 and the Comm API on port 46202. The Agent will leave all the log files in the `/tmp/Agent2` folder.


The `compss_agents_add_resources` and `compss_agents_remove_resources` can be used also to modify the resource pool with remote agents. The following command configures Agent1 to offload tasks onto Agent2 assuming that it has one single CPU core.
```
compss_agent_add_resources \
   --agent_node=127.0.0.1 \
   --agent_port=46101 \
   --cpu=1 \
   127.0.0.2 Port=46202
```

After executing the command, the resource query on Agent1 will return two resources.
```
{
  "resources": [
    {
      "name": "127.0.0.2",
      "description": {
        "storage_type": "[unassigned]",
        "storage_bandwidth": -1,
        "storage_size": -1,
        "memory_type": "[unassigned]",
        "processors": [
          {
            "name": "MainProcessor",
            "units": 1,
            "architecture": "[unassigned]"
          }
        ],
        "memory_size": -1
      },
      "adaptor": "es.bsc.compss.agent.comm.CommAgentWorker"
    },
    {
      "name": "127.0.0.1",
      "description": {
        "storage_type": "[unassigned]",
        "storage_bandwidth": -1,
        "storage_size": -1,
        "memory_type": "[unassigned]",
        "processors": [
          {
            "name": "MainProcessor",
            "units": 2,
            "architecture": "[unassigned]"
          }
        ],
        "memory_size": -1
      },
      "adaptor": "es.bsc.compss.types.COMPSsMaster"
    }
  ],
  "time": 1738683204561
}
```

Submitting one more execution of the `main_agents` method to the Agent1 should result in job23 for the `main_agents` and jobs 24 to 28 should contain the `add_delay` tasks. However, looking at the jobs folder of Agent1, we should see that job25 is not there. This task has been offloaded onto Agent2 and should appear in `/tmp/Agent2/jobs` folder.


Submitting another execution of the `main_agent` method with a higher value on the parameter it will create more tasks. The following command creates up to 20 `add_delay` tasks.
```
compss_agent_call_operation \
   --master_node=127.0.0.1 \
   --master_port=46101 \
   --method_name=main_agents \
   --lang=PYTHON \
   demo_app 20
```
With this new setup, the execution should take around 7 seconds and 6-8 tasks can be offloaded onto Agent2.

### 6. Dynamic adaptation of the resource pool
One of the key features of COMPSs agents is its ability to adapt to dynamically changing infrastructures. The last execution of this tutorial aims to showcase this feature. To that end, a very long execution of the `demo_app` will be submitted; for instance, generating 360 tasks.

```
compss_agent_call_operation \
   --master_node=127.0.0.1 \
   --master_port=46101 \
   --method_name=main_agents \
   --lang=PYTHON \
   demo_app 360
```

With the current resource pool in Agent1, the agent will be offload onto Agent2 about 1 third of the tasks. We will simulate that  for some reason -e.g., some network connectivity problem - Agent2 can no longer be used and it need to be remove from the pool while executing the method. That can be done with the `compss_agent_reduce_resources` command.
```
compss_agent_reduce_resources \
   --agent_node=127.0.0.1 \
   --agent_port=46101 \
   --cpu=1 \
   127.0.0.2
```
Whenever this command is executed, Agent1 will stop offloading tasks onto Agent2, and Agent2 should stop executing tasks. That can be verified by monitoring the number of jobs executed by Agent2 with the following command: `ls /tmp/Agent2/jobs/*.out | wc -l`.

Whenever Agent2 recovers from the error and is ready for receiving offloaded tasks from Agent1 again, we can use the `compss_agents_add_resources` one more time to increase the resource pool. 
```
compss_agent_add_resources \
   --agent_node=127.0.0.1 \
   --agent_port=46101 \
   --cpu=1 \
   127.0.0.2 Port=46202
```
Agent1 will start offloading tasks onto it again, and the number of executed jobs will grow again (`ls /tmp/Agent2/jobs/*.out | wc -l`).
