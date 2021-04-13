Algorithms
==========
[![Binder](https://mybinder.org/badge_logo.svg)](https://mybinder.org/v2/gh/karlstroetmann/Algorithms/HEAD)

These are the lecture notes for the class on "Algorithms and Complexity" that I am teaching at
the Baden-Wuerttemberg Cooperative State University (DHBW) in Manheim.  This repository contains
the following directories:

* `Lecture-Notes` contains the `LaTeX` sources of my lecture notes.
   The file `Lecture-Notes/algorithms.pdf` contains the lecture notes.
* `Python` contains the *Jupyter notebooks* that are presented in my class.   
* `Java` contains *Java* implementations of some of the algorithms presented in my lecture.
* `SetlX` contains [SetlX](https://randoom.org/Software/SetlX/) implementations of the programs
  presented in my lecture
  
# Docker (Experimental Feature)

The notebooks available in this repository can be run via [Docker](https://www.docker.com).
In order to be able to store changes made to these notebooks it is beneficial to create a local
directory `Solutions` that will contain the github repository.
The permissions of the directory `Solutions` need to be set to `777`, i.e. the directory needs
to be readable, writeable and executable for everybody.
For example, with `linux` creating this directory and setting the permissions
is achieved with the following commands:
```
    cd
    mkdir Solutions
    chmod 777 Solutions
```
Next, I suggest cloning the github repository into this directory.  This can be done as follows:
```
    cd Solution
    git clone https://github.com/karlstroetmann/Algorithms.git
```
The docker image can be downloaded via the following command:
```
    docker pull karlstroetmann/algorithms
```
After that, the *container* can be started as follows:
```
docker run -p 8888:8888 --rm -v /Users/yourname/Solutions/Algorithms/Python:/home/jovyan/Python/ karlstroetmann/algorithms
```
Note that you have to replace the directory `/Users/yourname` with your home directory.
Unfortunately, you cannot specify your home directory as `~`, sincer `Docker` does not
understand this notation.

Now the directory `/Users/yourname/Solutions/Algorithms/Python` contains all notebooks locally.
This directory should be regularly updated via `git pull`. 

To connect to the container, enter the adress `localhost:8888` into your browser.
You will then be asked for a *token*.  You find this token in the message that is
displayed by the `docker run ...` command executed previously.