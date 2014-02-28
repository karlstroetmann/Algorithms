#include <malloc.h>
#include <stdbool.h>
#include <assert.h>

typedef struct Node* NodePtr;

struct Node {
    Data    data;
    NodePtr nextPtr;
};

// A list is just a pointer to its first node.
typedef NodePtr List;

// A constructor for list nodes.
NodePtr createNode(Data data, NodePtr next)
{
    NodePtr nodePtr  = malloc( sizeof(struct Node) );
    nodePtr->data    = data;
    nodePtr->nextPtr = next;
    return nodePtr;
}

// add an element at the front of the list
List push(Data data, List l)
{
    return createNode(data, l);
}

// This destructor destroys a list and frees the associated nodes.
void deleteList(List l)
{
    if (l == 0)
		return;
    List rest = l->nextPtr;
	free(l);
	deleteList(rest);
};

void printList(List l)
{
	if (l == 0) {
		return;
	}
	printData(l->data);
	printf(" ");
	printList(l->nextPtr);
};

bool isOrdered(List l)
{
	if (l == 0)
		return true;
	if (l->nextPtr == 0)
		return true;
	Data d1 = l->data;
	Data d2 = l->nextPtr->data;
	int cmp = compareData(d1, d2);
	if (cmp <= 0) {
		return isOrdered(l->nextPtr);
	} else {
		return false;
	}
}

// Return a pointer to the node that contains the smallest data.
NodePtr getMin(List l)
{
	assert(l != 0);
	Data first  = l->data;
	if (l->nextPtr == 0) {
		return l;
	}
	NodePtr min_rest = getMin(l->nextPtr);
	Data    rest = min_rest->data;
	int cmp = compareData(first, rest);
	if (cmp <= 0) {
		return l;
	} else {
		return min_rest;
	}
}

// Delete the node *ptr from the list l.  Crashes if l does not contain
// *ptr.
List delete(List l, NodePtr ptr) 
{
	if (l == ptr) {
		return ptr->nextPtr;
	}
	assert(l->nextPtr != 0);
	l->nextPtr = delete(l->nextPtr, ptr);
	return l;
}

List cons(NodePtr ptr, List l) 
{
	ptr->nextPtr = l;
	return ptr;
}

List minSort(List l1) 
{
	List l2, l3, l4;
	if (l1 == 0)
		return l1;
	NodePtr m = getMin(l1);
	l2 = delete(l1, m);
	l3 = minSort(l2);
	l4 = cons(m, l3);
	return l4;
}

	
