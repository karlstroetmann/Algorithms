// A simple array based implementation of a stack.

typedef struct 
{
	unsigned stackPointer;  // the number of the next free place in the array
	unsigned size;          // the size of the allocated array
	Element * array;         // a pointer to the beginning of the array
} cat(Stack,Element);

cat(Stack,Element)* cat(create,Element)() {
	cat(Stack,Element)* S = malloc( sizeof(cat(Stack,Element)) );
	S->stackPointer = 0;
	S->size  = 16;
	S->array = malloc(S->size * sizeof(Element));
	return S;
}

void cat(push,Element)(cat(Stack,Element)* S, Element x) {
	if (S->stackPointer == S->size) {
		// array is full, allocate a larger array and copy all elements into
		// this new array. Doubling the size has worked well in practice.
		unsigned oldSize = S->size;
		S->size *= 2;
		Element* newArray = malloc(S->size * sizeof(Element));
		for (unsigned i = 0; i < oldSize; ++i) {
			newArray[i] = S->array[i];
		}
		free(S->array);
		S->array = newArray;
	}
	S->array[S->stackPointer] = x;
	++S->stackPointer;
}

void cat(pop,Element)(cat(Stack,Element)* S) {
	assert(S->stackPointer > 0);
	--S->stackPointer;
}

Element cat(top,Element)(cat(Stack,Element)* S) {
	assert(S->stackPointer > 0);
	return S->array[(S->stackPointer) - 1];
}

bool cat(isEmpty,Element)(cat(Stack,Element)* S) {
	return S->stackPointer == 0;
}

void cat(deleteStack,Element)(cat(Stack,Element)* S) {
	assert(S->stackPointer == 0);
	free(S->array);
	free(S);
}

void cat(printDashes,Element)(unsigned n) {
	for (unsigned i = 0; i < n; ++i) {
		printf("-");
	}
}

unsigned cat(computeNrDashes,Element)(cat(Stack,Element)* S) {
	unsigned number = 1;
	for (unsigned i = 0; i < S->stackPointer; ++i) {
		number += 3 + cat(size,Element)(S->array[i]);
	}
	return number;
}

void cat(printStack,Element)(cat(Stack,Element)* S) {
	unsigned n = cat(computeNrDashes,Element)(S);
	cat(printDashes,Element)(n);
	printf("\n|");
	for (unsigned i = 0; i < S->stackPointer; ++i) {
		cat(print,Element)(S->array[i]);
	}
	printf("\n");
	cat(printDashes,Element)(n);
	printf("\n");
}
