pdf: algorithms.pdf
	open algorithms.pdf; cp algorithms /Users/stroetma/AMADEUS/public-html/Algorithms/

%.pdf: %.ps
	ps2pdf $<

%.ps:  %.dvi
	dvips -o $@ $<

%.dvi: introduction.tex limits.tex oh-notation.tex hoare.tex sorting.tex \
       timsort.tex adt.tex map.tex map-avl.tex wolf-ziege-kohl.tex \
       prioqueue.tex huffman.tex graph.tex monte-carlo.tex
	latex algorithms.tex
