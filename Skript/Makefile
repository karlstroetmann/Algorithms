run: algorithmen.pdf
	open algorithmen.pdf

algorithmen.pdf: algorithmen.ps
		ps2pdf algorithmen.ps

algorithmen.ps: algorithmen.dvi
		dvips -o algorithmen.ps algorithmen.dvi

algorithmen.dvi: algorithmen.tex einfuehrung.tex grenzen.tex komplexitaet.tex adt.tex sorting.tex map.tex map-avl.tex hoare.tex huffman.tex wolf-ziege-kohl.tex prioqueue.tex graph.tex
		latex algorithmen.tex