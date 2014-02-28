// A simple array based implementation of a stack.

typedef struct 
{
	unsigned stackPointer;  // the number of the next free place in the array
	unsigned size;          // the size of the allocated array
	Element * array;         // a pointer to the beginning of the array
} Stack;

Stack* create() {
	Stack* S = malloc( sizeof(Stack) );
	S->stackPointer = 0;
	S->size  = 16;
	S->array = malloc(S->size * sizeof(Element));
	return S;
}

void push(Stack* S, Element x) {
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

void pop(Stack* S) {
	assert(S->stackPointer > 0);
	--S->stackPointer;
}

Element top(Stack* S) {
	assert(S->stackPointer > 0);
	return S->array[(S->stackPointer) - 1];
}

bool isEmpty(Stack* S) {
	return S->stackPointer == 0;
}

void deleteStack(Stack* S) {
	assert(S->stackPointer == 0);
	free(S->array);
	free(S);
}

void printDashes(unsigned n) {
	for (unsigned i = 0; i < n; ++i) {
		printf("-");
	}
}

unsigned computeNrDashes(Stack* S) {
	unsigned number = 1;
	for (unsigned i = 0; i < S->stackPointer; ++i) {
		number += 3 + elementSize(S->array[i]);
	}
	return number;
}

void printStack(Stack* S) {
	unsigned n = computeNrDashes(S);
	printDashes(n);
	printf("\n|");
	for (unsigned i = 0; i < S->stackPointer; ++i) {
		printElement(S->array[i]);
	}
	printf("\n");
	printDashes(n);
	printf("\n");
}
