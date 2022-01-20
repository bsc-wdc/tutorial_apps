# COMPSs Tutorial applications

This folder contains tutorial applications developed for [py]COMPSs.

Its purpose is to share the tutorial applications among the COMPSs users community to ease
the implementation of new applications and share them with the rest of users.


## Tutorial Application's Folder Structure

Application are grouped by language (C, Java and Python).
Each of them includes a set of applications used in tutorials following the next
structure:

```
apps
  |- python
  |    |- app_1
  |    |    |- src                 Application source code
  |    |    |- README              Brief description of the application
  |- java
  |    |- app_1
  |    |    | ...
  |- c
       |- ...
```

## Tutorial Hands-on Instructions and Commands

### Setup Instructions

Update Tutorial apps:

```
$ rm -rf tutorial_apps 
$ git clone https://github.com/bsc-wdc/tutorial_apps.git
```
Install COMPSS-PyCOMPSS CLI:

```
$ sudo python3 -m pip install pycompss-cli
```

### Python Hands-on

Initialize a PyCOMPSs docker environment.

```
$ pycompss init -n docker-tutorial docker -i compss/compss-tutorial:2.10
```

Start monitor.

```
$ pycompss monitor start
```

Start Jupyter Notebook.

```
$ pycompss jupyter
```

Once started, open a web browser with the address: http://localhost:8888
Follow the instructions of the Jupyter notebook, and at the end of the hands-on session, follow the instructions to clean the environment. 

Stop the monitor.

```
$ pycompss monitor stop
```

Remove the PyCOMPSs docker environment.

```
$ pycompss environment remove docker-tutorial 
```


### Java Hands-on 


Compile the application.

```
$ cd /home/compss/tutorial_apps/java/wordcount
$ mvn clean install
```

Initialize a COMPSs docker environment. 

```
$ compss init -n docker-tutorial docker -i compss/compss-tutorial:2.10
```

Follow the exercises to develop the wordcount application. 

Run the application.

```
$ compss run --classpath=jar/wordcount.jar wordcount.uniqueFile.Wordcount data-set/file_small.txt 650
```

To enable an execution with monitoring:

Start the monitor.

```
$ compss monitor start
```
Open a browser in URL http://localhost:8080/compss-monitor

Run the application with monitoring flags.

```
$ compss run -m --classpath=jar/wordcount.jar wordcount.uniqueFile.Wordcount data-set/file_long.txt 350000
```

Simulate the execution with different workers.
 
- To check the used resources run use the following commands:

```
$ compss exec cat /project.xml
$ compss exec cat /resources.xml
```

- Include two worker nodes.
```
$ compss components add worker 2
```
- Run again the application with monitoring flags

```
$ compss run -m --classpath=jar/wordcount.jar wordcount.uniqueFile.Wordcount data-set/file_long.txt 350000
```

Run the application with graph generation and generate the graph  
```
$ compss run -g --classpath=jar/wordcount.jar wordcount.uniqueFile.Wordcount data-set/file_small.txt 650
$ compss gengraph .COMPSs/wordcount.uniqueFile.Wordcount_04/monitor/complete_graph.dot
$ evince .COMPSs/wordcount.uniqueFile.Wordcount_04/monitor/complete_graph.pdf
```
Run the application with debug information

```
$ compss run -d --classpath=jar/wordcount.jar wordcount.uniqueFile.Wordcount /home/compss/workspace_java/wordcount/data/file_small.txt 650
```

Stop the monitor

```
$ compss monitor stop
```

Remove the docker environment

```
$ compss environment remove docker-tutorial
```


### C++ exercise 

Enter in the application folder and start the COMPSs docker environment

```
$ cd ~/tutorial_appss/c/matmul_object
$ compss init -n docker-tutorial docker -i compss/compss-tutorial:2.10
```
Build the application

```
$ compss exec compss_build_app Matmul
```

Run the application

```
$ compss run --appdir=/home/user/ master/Matmul 4 4 2.0
```

Remove the docker environment

```
$ compss environment remove docker-tutorial
```






a
