#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <assert.h>

// A simple array based implementation of a stack.

// Edit this line to set the type of the data that is stored in the stack.
#define Element int

// The following is quite obscure.  The macro cat(x,y) concatenates
// its arguments.  We need the macro cataux, because the definition of 
// cataux involves "##" and the arguments of a macro containing "##"
// are not macro expanded.  Since the definition of cat does not involve 
// "##", its arguments are expanded.

#define cataux(x, y) x ## y
#define cat(x, y) cataux(x, y)

void cat(print,Element)(Element n) {
	printf(" %d |", n);
}

unsigned cat(size,Element)(Element n) {
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

#include "stack-array-macro.c"

#undef Element
#define Element float

void cat(print,Element)(Element n) {
	printf(" %.2e |", n);
}

unsigned cat(size,Element)(Element n) {
	return 8;
}

#include "stack-array-macro.c"

int main() {
		cat(Stack,int)* S1 = cat(create,int)();
		for (int i = 0; i < 32; ++i) {
			cat(printStack,int)(S1);
			printf("\n");
			cat(push,int)(S1, i);
		}
		cat(Stack,float)* S2 = cat(create,float)();
		for (int i = 0; i < 10; ++i) {
			cat(printStack,float)(S2);
			printf("\n");
			cat(push,float)(S2, i);
		}
	
	return 0;
}
