#!/bin/bash
conda create -y -n algo python=3.9 jupyter notebook 
conda activate algo
conda install -c anaconda -y graphviz  
conda install -c conda-forge -y python-graphviz ipycanvas matplotlib seaborn
