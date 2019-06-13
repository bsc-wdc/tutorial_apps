# Python Tutorial Applications

This folder contains all PyCOMPSs related tutorial applications and notebooks.


## How to execute the sample apps

### Prerequisites

* Python
* COMPSs installation

For more information, please check the [Installation Manual](http://compss.bsc.es/releases/compss/latest/docs/COMPSs_Installation_Manual.pdf)

* Jupyter notebook (only for notebooks)

### Commands to run the PyCOMPSs applications

* Increment

```
cd ~/tutorial_apps/python/increment/
runcompss src/increment.py 10 1 2 3
```

* Matmul files

```
cd ~/tutorial_apps/python/matmul_files/
runcompss src/matmul_files.py 4 4
```

* Matmul objects

```
cd ~/tutorial_apps/python/matmul_objects/
runcompss src/matmul_objects.py 16 4
```

* Neurons

```
cd ~/tutorial_apps/python/neurons/
runcompss src/neurons.py 128 $(pwd)/data/spikes.dat
```

* Simple

```
cd ~/tutorial_apps/python/simple/
runcompss --lang=python src/simple.py 1
```

* Wordcount

```
cd ~/tutorial_apps/python/wordcount/
runcompss src/wordcount.py $(pwd)/data/
```

* Wordcount blocks

```
cd ~/tutorial_apps/python/wordcount/
runcompss src/wordcount_blocks.py $(pwd)/data/compss.txt result.txt 300000
```

* Wordcount merge

```
cd ~/tutorial_apps/python/wordcount/
runcompss src/wordcount_merge.py $(pwd)/data/
```



### Commands to run the PyCOMPSs notebooks

The Jupyter notebooks with PyCOMPSs are located into the ```tutorial_apps/python/notebooks``` folder.

To run them, just start the Jupyter notebook server and open the desired notebook exploring the folder structure:

```
cd $HOME
jupyter-notebook
```

Alternatively, it is possible to open a single notebook:

```
jupyter-notebook <NOTEBOOK_NAME>.ipynb
```


## Tracing PyCOMPSs Apps

1. Add the ```-t``` option to the runcompss (or the ```tracing=True``` flag when starting the runtime in the notebooks).
2. Execute paraver:
   ```
   wxparaver ~/.COMPSs/APPNAME_EXEC/trace/*.prv
   ```
3. Load any paraver configuration from:
   ```
   /opt/COMPSs/Dependencies/paraver/cfgs/
   ```


## Monitoring PyCOMPSs Apps

1. Enable the COMPSs Monitor:
   ```
   /etc/init.d/compss-monitor start
   ```
2. Add the ```-m``` option to the runcompss (or the ```monitor=<REFRESH_RATE>``` flag when starting the runtime in the notebooks).
3. Open the webpage:
   ```
   firefox http://localhost:8080/compss-monitor
   ```

## VM instructions

### Change keyboard layout

```
Menu -> Settings > Settings Manager Keyboard -> Layout
```

### Applications code can be seen with Eclipse

* Open eclipse by clicking the icon of the desktop
* Select the workspace located in ```~/tutorial_apps/python/```

### Run the applications from Eclipse (without tracing)

* Run -> External Tools -> compss-matmul
* Run -> External Tools -> compss-neurons



--------------------------------------------------------------

Please find more details about the COMP Superscalar framework at:

<http://compss.bsc.es>

Contact us at:

<support-compss@bsc.es>
