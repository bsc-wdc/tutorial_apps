# Python Tutorial Applications

This folder contains all PyCOMPSs related tutorial applications and notebooks.


## How to execute the sample apps

### Prerequisites

* Python
* COMPSs installation

For more information, please check the [Installation Manual](http://compss.bsc.es/releases/compss/latest/docs/COMPSs_Installation_Manual.pdf)

* Jupyter notebook (only for notebooks)

### Commands to run the PyCOMPSs applications

* Simple

```
cd ~/tutorial_apps/python/simple/
runcompss --lang=python src/simple.py 1
```

* Increment

```
cd ~/tutorial_apps/python/increment/
runcompss src/increment.py 10 1 2 3
```

* Word Count

```
cd ~/tutorial_apps/python/wordcount/
runcompss src/wordcount.py $(pwd)/data/
```

* Matmul objects

```
cd ~/tutorial_apps/python/matmul-objects/
runcompss src/matmul.py 16 4
```

* Matmul files

```
cd ~/tutorial_apps/python/matmul-files/
runcompss src/matmul.py 4 4
```

* Neurons

```
cd ~/tutorial_apps/python/neurons/
runcompss src/ns-data-proc_compss_objects.py 128 /sharedDisk/neurons/spikes.dat
```

### Commands to run the PyCOMPSs notebooks

Just start the Jupyter notebook server and open the desired notebook exploring the folder structure:

```
cd $HOME
jupyter-notebook
```

Alternatively, it is possible to open a single notebook:

```
jupyter-notebook <NOTEBOOK_NAME>.ipynb
```


## Tracing PyCOMPSs Apps

1. Add the ```-t``` option to the runcompss.
2. Execute paraver:
   ```
   wxparaver ~/.COMPSs/APPNAME_EXEC/trace/*.prv
   ```
3. Load any paraver configuration from:
   ```
   /opt/COMPSs/Dependencies/paraver/cfgs/
   ```


## Monitoring PyCOMPSs Apps

1. Add the ```-m``` option to the runcompss.
2. Enable the COMPSs Monitor:
   ```
   /etc/init.d/compss-monitor start
   ```
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
