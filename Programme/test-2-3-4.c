#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>
#include <math.h>
#include <time.h>
#include <stdbool.h>
#include <assert.h>

typedef unsigned Key;
typedef char     Value;

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

// #include "2-3-4-dict.c"
#include "2-3-4-dict-delete.c"
// #include "2-3-4-union.c"

// Generate some random trees and display them.
void createRandomTrees() 
{
	unsigned count;
	Tree t = 0;
	while (1) {
		printf("Wie viele Knoten einfügen? ");
		fflush(stdin);
		scanf("%u", &count);
		for (unsigned i = 0; i < count; ++i) {
			Key   key = rand() % (10 * count);
			Value val = 'a';   // The value is unimportant for this experiment.
			t         = insert(t, key, val);
//			printTableDot(t, i);
		}
		printTableDot(t, count);
		printf("Wie viele Knoten löschen? ");
		fflush(stdin);
		scanf("%u", &count);
		for (unsigned i = 0; i < count; ++i) {
			Key   key;
			printf( "Welcher Knoten soll gelöscht werden? " );
			scanf(  "%u", &key );
			t         = delete(t, key);
		}
		printTableDot(t, count);
	}
}

void interactive() 
{
	unsigned count = 0;
	Table    t     = 0;
	printf("Wie viele Knoten? ");
	fflush(stdin);
	scanf("%u", &count);
	for (unsigned i = 0; i < count * 2; ++i) {
		Key   key = rand() % (count * 5);
		Value val = 'a';   // The value is unimportant for this experiment.
		t         = insert(t, key, val);
	}
// 	for (unsigned i = 0; i < count; ++i) {
// 		t         = delete(t, i);
// 	}
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
//	createRandomTrees();
	interactive();
}
