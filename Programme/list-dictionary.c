#include <malloc.h>
#include <stdbool.h>
#include <assert.h>

// A list based implementation of the abstract data type "Dictionary".
// For this Implementation, the type Table is implemented as a List.

typedef struct Node* NodePtr;

struct Node {
    Key      key;
    Value    val;
    NodePtr  nextPtr;
};

// A list is just a pointer to its first node.
typedef NodePtr List;
typedef List    Table;

Table insert(Table t, Key k, Value v) 
{
    NodePtr nodePtr  = malloc( sizeof(struct Node) );
    nodePtr->key     = k;
    nodePtr->val     = v;
    nodePtr->nextPtr = t;
    return nodePtr;
}

Value* search(Table t, Key k)
{
	if (t == 0)
		return 0;
	if (compareKey(t->key, k)) 
		return &(t->val);
	else 
		return search(t->nextPtr, k);
}

// This operation requires that the table contains an entry with key k.
// If this requirement is not satisfied, this function crashes.
Table delete(Table t, Key k)
{
	Table res;
	if (compareKey(t->key, k)) {
		res = t->nextPtr;
		free(t);
		return res;
	} else {
		t->nextPtr = delete(t->nextPtr, k);
		return t;
	}
}

// Return all allocated memory.
void freeList(List l) 
{
	if (l == 0)
		return;
	List next = l->nextPtr;
	free(l);
	freeList(next);
}

// used for debugging
void printTableAux(Table t) 
{
	if (t == 0)
		return;
	printf("< ");
	printKey(t->key);
	printf(", ");
	printVal(t->val);
	printf(" >");
	if (t->nextPtr != 0) {
		printf(", ");
	}
	printTableAux(t->nextPtr);
}

// used for debugging
void printTable(Table t) 
{
	printf("[ ");
	printTableAux(t);
	printf(" ]\n");
}

