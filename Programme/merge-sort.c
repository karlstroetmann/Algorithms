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
		printf("\n");
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


List cons(NodePtr ptr, List l) 
{
	ptr->nextPtr = l;
	return ptr;
}

void split(List l, List* l1_ptr, List* l2_ptr)
{
	if (l == 0) {
		*l1_ptr = 0;
		*l2_ptr = 0;
		return;
	}
	if (l->nextPtr == 0) {
		*l1_ptr = l;
		*l2_ptr = 0;
		return;
	}
	List l1_rest;
	List l2_rest;
	split(l->nextPtr->nextPtr, &l1_rest, &l2_rest);
	NodePtr frst = l;
	NodePtr scnd = l->nextPtr;
	*l1_ptr = cons(frst, l1_rest);
	*l2_ptr = cons(scnd, l2_rest);
}

List merge(List l1, List l2)
{
	if (l1 == 0) 
		return l2;
	if (l2 == 0) 
		return l1;
	Data d1 = l1->data;
	Data d2 = l2->data;
	int cmp = compareData(d1, d2);
	if (cmp <= 0) {
		return cons(l1, merge(l1->nextPtr, l2));
	} else {
		return cons(l2, merge(l1, l2->nextPtr));
	}
}

List mergeSort(List l)
{
	if (l == 0 || l->nextPtr == 0)
		return l;
	List l1, l2, l3, l4, l5;
	split(l, &l1, &l2);
	l3 = mergeSort(l1);
	l4 = mergeSort(l2);
	l5 = merge(l3, l4);
	return l5;
}

	
