# Python Tutorial Applications

## CHANGE KEYBOARD LAYOUT

 Menu -> Settings > Settings Manager Keyboard -> Layout


## EXECUTE PYTHON SAMPLE APPS

### Applications code can be seen with Eclipse

* Open eclipse by clicking the icon of the desktop
* Select the workspace located in ```~/tutorial_apps/python/```

### Commands to run the pyCOMPSs applications

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

### Run the applications from Eclipse (without tracing)

* Run -> External Tools -> compss-matmul
* Run -> External Tools -> compss-neurons


## TRACING COMPSs APPS

1. Add the ```-t``` option to the runcompss
2. Execute paraver

   ```wxparaver ~/.COMPSs/APPNAME_EXEC/trace/*.prv```

3. Load any paraver configuration from

   ```/opt/COMPSs/Dependencies/paraver/cfgs/```


## MONITORING COMPSs APPS

1. Add the ```-m``` option to the runcompss
2. Enable the COMPSs Monitor

   ```/etc/init.d/compss-monitor start```

3. Open the webpage

   ```firefox http://localhost:8080/compss-monitor```


--------------------------------------------------------------

Please find more details about the COMP Superscalar framework at:

    	<http://compss.bsc.es>

 Contact us at:

	    <support-compss@bsc.es>
