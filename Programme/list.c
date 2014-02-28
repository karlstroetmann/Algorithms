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

List insert(NodePtr n, List l)
{
    if (l == 0) {
        n->nextPtr = 0;
        return n;
    }
    int cmp = compareData(n->data, l->data);
    if (cmp <= 0) {
        n->nextPtr = l;
        return n;
    } else {
        NodePtr newNext = insert(n, l->nextPtr);
        l->nextPtr = newNext;
        return l;
    }
}

List sortList(List l)
{
    if (l == 0)
        return l;
    NodePtr next   = l->nextPtr;
    NodePtr sorted = sortList(next);
    return insert(l, sorted);
}

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

	
