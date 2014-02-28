#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>
#include <math.h>
#include <time.h>
#include <stdbool.h>
#include <assert.h>

typedef unsigned Key;
typedef char     Value;

void printKey(Key k) 
{
	printf("%u", k);
}

void printVal(Value v) 
{
	printf("'%c'", v);
}

int compareKey(Key c1, Key c2) 
{
	if (c1 < c2)
		return -1;
	if (c1 > c2)
		return  1;
	return 0;
}

unsigned max(unsigned a, unsigned b)
{
	if (a > b)
		return a;
	return b;
}

#include "tree-dictionary.c"


// This function computes the average path length for a number of randomly generated 
// ordered binary trees.  To run this test, the assertion in insert has to be 
// switched off.  This is done by compiling with -DNDEBUG, see below:
//
//     gcc -std=c99 -DNDEBUG -O4 -Wall -lm -o test-dict-tree test-dict-tree.c
//
void averagePathLength() 
{
	printf("  +--------------------------+-------------------+----------------+ \n");
	printf("  | l: best possible height  | a: average height |        a / l   |\n");
	printf("  +==========================+===================+================+ \n");
	unsigned number = 0;
	for (unsigned l = 0; l <= 20; ++l) {
		number = 2 * number + 1;
		Table t = 0;
		for (unsigned i = 0; i < number; ++i) {
			Key   key = rand();
			Value val = 'a';   // The value is unimportant for this experiment.
			t         = insert(t, key, val);
		}
		double length        = totalLength(t);
		double count         = nodeCount(t);
		double averageHeight = length / count;
		double r             = averageHeight / (l-1);
		printf("  | %23u  |           %6.3f  |       %8.5f |\n", l, averageHeight, r);
        printf("  +--------------------------+-------------------+----------------+ \n");
		deleteAll(t);
	}
}

// Generate some random trees and display them.
void createRandomTrees() 
{
	unsigned count;
	while (1) {
		printf("How many nodes? ");
		scanf("%u", &count);
		BinTree t = 0;
		for (unsigned i = 0; i < count; ++i) {
			Key   key = rand() % count;
			Value val = 'a';   // The value is unimportant for this experiment.
			t         = insert(t, key, val);
		}
		printTableDot(t, count);
	}
}

// Generate a degenerated binary tree and display it.
void createDegenerateTree() 
{
	unsigned count;
	while (1) {
		printf("How many nodes? ");
		scanf("%u", &count);
		BinTree t = 0;
		for (unsigned i = 0; i < count; ++i) {
			Key   key = i;
			Value val = 'a';   // The value is unimportant for this experiment.
			t         = insert(t, key, val);
		}
		printTableDot(t, count);
	}
}

// This function allows to manipulate trees interactively.
void interactive() 
{
	unsigned count = 0;
	Table    t     = 0;
	// push lower case letters
	for (unsigned i = 0; i < 10; ++i) {
		Key   key = rand() % 25;
		Value val = 'a' + key;
		t = insert(t, key, val);
	}
	printTableDot(t, count++);
	Key k;
	do {
		unsigned i;
		printf( "Einfügen (1) oder löschen (0)? " );
		scanf(  "%u", &i );
		if (i == 1) {
			printf( "Welcher Knoten soll eingefügt werden? " );
			scanf(  "%u", &k );
			Value val = 'a' + k;
			t = insert(t, k, val);
			printTableDot(t, count++);
		} else {
			printf( "Welcher Knoten soll gelöscht werden? " );
			scanf(  "%u", &k );
			t = delete(t, k);
			printTableDot(t, count++);
		}
	} while (k != 0);
}

int main() 
{
//	averagePathLength();
//	createDegenerateTree();
//	createRandomTrees();
	interactive();
}
