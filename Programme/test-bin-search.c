#include <stdio.h>
#include <stdlib.h>

typedef unsigned Key;
typedef char     Value;

int compareKey(Key k1, Key k2) {
    if (k1 < k2)            return -1;
        else if (k1 == k2)  return 0;
        else                return +1;    
}   
	
#include "bin-search.c"

// Generate some random trees and display them.
void createRandomTrees() 
{
	unsigned count;
	while (1) {
		printf("How many nodes? ");
		scanf("%u", &count);
		List l = 0;
		for (unsigned i = 0; i < count; ++i) {
//			Key   key = rand() % count;
			Key   key = i;
			Value val = 'a';   // The value is unimportant for this experiment.
			l         = push(key, val, l);
		}
		List     sorted = mergeSort(l);
		unsigned number = length(sorted);
		BinTree t       = list2tree(sorted, number);
		printTableDot(t, count);
	}
}

int main() 
{
	createRandomTrees();
	return 0;
}
