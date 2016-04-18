loesung-2011: loesung-2011.pdf
	open loesung-2011.pdf

loesung-2009: loesung-2009.pdf
	open loesung-2009.pdf

aufgaben: aufgaben.pdf
	open aufgaben.pdf

%.pdf: %.ps
	ps2pdf $<

%.ps: %.dvi
	dvips -o $@ $<

%.dvi: %.tex
	latex $<

