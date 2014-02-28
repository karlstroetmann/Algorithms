#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <assert.h>

// A simple array based implementation of a stack.

// Edit this line to set the type of the data that is stored in the stack.
typedef int Element;

void printElement(Element n) 
{
	printf(" %d |", n);
}

unsigned elementSize(Element n) 
{
	unsigned size = (n < 0) ? 1 : 0;
	if (n == 0) {
		++size;
	}
	while (n > 0) {
		++size;
		n /= 10;
	}
	return size;
}

#include "stack-array.c"

int main() 
{
	Stack* S = create();
	for (int i = 0; i < 32; ++i) {
		printStack(S);
		printf("\n");
		push(S, i);
	}
	for (int i = 0; i < 32; ++i) {
		printStack(S);
		printf("top = %d\n", top(S));
		printf("\n");
		pop(S);
	}
	
	return 0;
}
