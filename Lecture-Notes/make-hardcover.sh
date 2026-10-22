#!/bin/sh
# Build algorithms-hardcover.pdf, the interior of the hardcover edition (8.25" x 11", KDP's largest
# hardcover size).  It differs from algorithms.pdf (US Letter, paperback) only in the page width,
# so all line and page breaks are the same.  Adapted from the Logic notes.
set -e
cd "$(dirname "$0")"
export PATH=/Library/TeX/texbin:$PATH
job=algorithms-hardcover
run_latex() {
    pdflatex -shell-escape -interaction=nonstopmode -jobname=$job '\def\hardcover{}\input{algorithms}' \
        > /dev/null || { echo "pdflatex failed, see $job.log"; exit 1; }
}
run_latex
bibtex $job > /dev/null
makeindex $job > /dev/null 2>&1
run_latex
run_latex
# The first run has no table of contents yet, so the index is regenerated until its page
# numbers no longer change.
for i in 1 2 3; do
    cp $job.ind $job.ind.old
    makeindex $job > /dev/null 2>&1
    if cmp -s $job.ind $job.ind.old; then break; fi
    run_latex
done
rm -f $job.ind.old
grep -E '^!|Overfull|Underfull|Rerun to get' $job.log || echo "$job.log: no errors, no bad boxes"
echo "written: $(pwd)/$job.pdf"
