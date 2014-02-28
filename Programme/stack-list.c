// A stack implementation based on linked lists.

typedef struct cat(Node,Element) {
    Element                   data;
    struct cat(Node,Element)* nextPtr;
} cat(Node,Element);

// A stack is a struct containing a pointer to the first node of the 
// associated list.
typedef struct {
	cat(Node,Element)* first;
} cat(Stack,Element);

cat(Stack,Element)* cat(create,Element)() {
	cat(Stack,Element)* S = malloc( sizeof(cat(Stack,Element)) );
	S->first = 0;
	return S;
}

// A constructor for list nodes, needed by push().
cat(Node,Element)* cat(createNode,Element)(
	Element            data, 
	cat(Node,Element)* next) 
{
    cat(Node,Element)* nodePtr = malloc( sizeof(cat(Node,Element)) );
    nodePtr->data    = data;
    nodePtr->nextPtr = next;
    return nodePtr;
}

// add an element at the front of the list
void cat(push,Element)(cat(Stack,Element)* S, Element x) {
    cat(Node,Element)* newNode = cat(createNode,Element)(x, S->first);
	S->first = newNode;
}

void cat(pop,Element)(cat(Stack,Element)* S) {
	assert(S->first != 0);
	cat(Node,Element)* tail = S->first->nextPtr;
	free(S->first);
	S->first = tail;
}

Element cat(top,Element)(cat(Stack,Element)* S) {
	assert(S->first != 0);
	return S->first->data;
}

bool cat(isEmpty,Element)(cat(Stack,Element)* S) {
	return S->first == 0;
}

void cat(deleteStack,Element)(cat(Stack,Element)* S) {
	assert(S->first == 0);
	free(S);
}

void cat(printDashes,Element)(unsigned n) {
	for (unsigned i = 0; i < n; ++i) {
		printf("-");
	}
}

unsigned cat(computeNrDashes,Element)(cat(Stack,Element)* S) {
	unsigned number = 1;
	for (cat(Node,Element)* next = S->first; next; next = next->nextPtr) {
		number += 3 + cat(size,Element)(next->data);
	}
	return number;
}

void cat(printStackAux,Element)(cat(Node,Element)* next) {
	if (next == 0) {
		return;
	}
	cat(printStackAux,Element)(next->nextPtr);
	cat(print,Element)(next->data);
}

void cat(printStack,Element)(cat(Stack,Element)* S) {
	unsigned n = cat(computeNrDashes,Element)(S);
	cat(printDashes,Element)(n);
	printf("\n|");
	cat(printStackAux,Element)(S->first);
	printf("\n");
	cat(printDashes,Element)(n);
	printf("\n");
}
