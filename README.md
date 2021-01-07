Algorithms
==========
[![Binder](https://mybinder.org/badge_logo.svg)](https://mybinder.org/v2/gh/karlstroetmann/Algorithms/HEAD)

These are the lecture notes for the class on "Algorithms and Complexity" that I am teaching at
the Baden-Wuerttemberg Cooperative State University (DHBW) in Manheim.  This repository contains
the following directories:

* `Lecture-Notes` contains the $Latex$ sources of my lecture notes.
   The file `Lecture-Notes/algorithms.pdf` contians the lecture notes.
* `Python` contains the *Jupyter notebooks* that are presented in my class.   
* `Java` contains *Java* implementations of some of the algorithms presented in my lecture.
* `SetlX` contains [SetlX](https://randoom.org/Software/SetlX/) implementations of the programs
  presented in my lecture
  
# Docker (Experimental Feature)

The notebooks available in this repository can be run via [Docker](https://www.docker.com).
For this it is beneficial to create a local directory `Solutions` that will we linked to the
directory `Python/Personal-Solutions`.  The permissions of the directory `Solutions` need
to be set to `777`.  For example, with `linux` creating this directory and setting the permissions
is achievd with the following commands:
```
    cd
    mkdir Solutions
    chmod 777 Solutions
```
Then, in order to change a file and have the changes persist, the file has to be copied to
`Python/Personal-Solutions`.  Then the docker image can be downloaded via the following command:
```
    docker pull karlstroetmann/algorithms
```
After that, the *container* can be started as follows:
```
docker run -p 8888:8888 -v /Users/stroetmann/Solutions/:/home/jovyan/Python/Personal-Solutions karlstroetmann/algorithms
```
To connect to the container, enter the adress `localhost:8888` into your browser.
You will be asked for a *token*.  You find this token in the message that is displayed by the `docker run ...` command.