run: algorithm-slides.pdf
	open algorithm-slides.pdf


algorithm-slides.pdf: algorithm-slides.ps
	ps2pdf algorithm-slides.ps

algorithm-slides.ps: algorithm-slides.dvi
	dvips -o algorithm-slides.ps algorithm-slides.dvi

algorithm-slides.dvi: algorithm-slides.tex
	latex algorithm-slides.tex