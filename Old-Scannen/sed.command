cat 0bit.html | sed 's/\([0-9]\+\) \([0-9]\+\)/2^\1 - \2/g' | bc
