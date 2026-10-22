#!/bin/sh
# Build the e-book algorithms-python.epub from the LaTeX sources of the notes.
# Needs pandoc 3 (Homebrew), pdflatex, epstopdf, pdfcrop, pdfinfo, and pdftocairo.
# Run it after the printed book has been built.
set -e
cd "$(dirname "$0")"
python3 prepare.py
cd build
/opt/homebrew/bin/pandoc book.tex \
    --from latex --to epub3 \
    --metadata-file=../metadata.yaml \
    --css=../ebook.css \
    --mathml \
    --top-level-division=chapter --number-sections \
    --toc --toc-depth=2 \
    --split-level=1 \
    --citeproc --bibliography=refs.bib \
    -o ../algorithms-python.epub
echo "written: $(cd .. && pwd)/algorithms-python.epub"
