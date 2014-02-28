#include <malloc.h>
#include <stdbool.h>
#include <assert.h>

typedef struct Node* NodePtr;

struct Node {
    Key     key;
    Value   val;
    NodePtr nextPtr;
};

// A list is just a pointer to its first node.
typedef NodePtr List;

// A constructor for list nodes.
NodePtr createNode(Key key, Value val, NodePtr next)
{
    NodePtr nodePtr  = malloc( sizeof(struct Node) );
    nodePtr->key     = key;
    nodePtr->val     = val;
    nodePtr->nextPtr = next;
    return nodePtr;
}

// add an element at the front of the list
List push(Key key, Value val, List l)
{
    return createNode(key, val, l);
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

bool isOrdered(List l)
{
	if (l == 0)
		return true;
	if (l->nextPtr == 0)
		return true;
	Key k1 = l->key;
	Key k2 = l->nextPtr->key;
	int cmp = compareKey(k1, k2);
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

// This implementation of merge removes duplicate elements.
List merge(List l1, List l2)
{
	if (l1 == 0) 
		return l2;
	if (l2 == 0) 
		return l1;
	Key k1 = l1->key;
	Key k2 = l2->key;
	int cmp = compareKey(k1, k2);
	if (cmp < 0) {
		return cons(l1, merge(l1->nextPtr, l2));
	} else if (cmp > 0) {
		return cons(l2, merge(l1, l2->nextPtr));
	} else if (cmp == 0) {
		return cons(l1, merge(l1->nextPtr, l2->nextPtr));
	} else {
		assert(0);
		return 0;
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

// Count the number of nodes in the given list.
unsigned length(List l)
{
	if (l == 0)
		return 0;
	return 1 + length(l->nextPtr);
}

// Find the ith node on the given List.
NodePtr ithNode(List l, unsigned i)
{
	if (i == 1)
		return l;
	return ithNode(l->nextPtr, i - 1);
}

typedef struct BinNode* BinNodePtr;

struct BinNode {
    Key        key;
    Value      val;
    BinNodePtr left;
    BinNodePtr right;
};

typedef BinNodePtr BinTree;

// A constructor for list nodes.
BinTree createBinNode(Key key, Value val, BinTree left, BinTree right)
{
    BinTree nodePtr = malloc( sizeof(struct BinNode) );
    nodePtr->key    = key;
    nodePtr->val    = val;
    nodePtr->left   = left;
    nodePtr->right  = right;
    return nodePtr;
}

// This destructor destroys a list and frees the associated nodes.
void deleteTree(BinTree b)
{
    if (b == 0)
		return;
    BinTree left  = b->left;
    BinTree right = b->right;
	free(b);
	deleteTree(left);
	deleteTree(right);
};

// Convert a given list into a binary tree.
// The list needs to be ordered.  The binary tree will be ordered, too.
// The second argument gives the length of the list.
BinTree list2tree(List l, unsigned length)
{
	if (length == 0)
		return 0;
	unsigned middle = length / 2 + length % 2;
	NodePtr  node   = ithNode(l, middle);
	BinTree  left   = list2tree(l, middle - 1);
	BinTree  right  = list2tree(node->nextPtr, length - middle);
	return createBinNode(node->key, node->val, left, right);	
}

// Search for a given key in the ordered binary tree t.
BinTree search(Key key, BinTree t) 
{
	if (t == 0)
		return 0;
	int cmp = compareKey(key, t->key);
	if (cmp == -1)
		return search(key, t->left);
	if (cmp == 1)
		return search(key, t->right);
	// Now the only possibility that is left is cmp == 0, 
    // that is key == t->key.
	return t;
}

static int counterZeros = 0;

void printTreeDot(FILE* handle, BinTree b) 
{
	if (b == 0)
		return;
	if (b->left != 0) {
		fprintf(handle, "    %u -> %u;\n", b->key, b->left->key);
	} else {
		fprintf(handle, "    %u -> %d;\n", b->key, --counterZeros);
	}
	if (b->right != 0) {
		fprintf(handle, "    %u -> %u;\n", b->key, b->right->key);
	} else {
		fprintf(handle, "    %u -> %d;\n", b->key, --counterZeros);
	}
	printTreeDot(handle, b->left);
	printTreeDot(handle, b->right);
}

// Create a dot file conatining the tree.
void printTableDot(BinTree b, unsigned count)
{
	counterZeros = 0;
	char fileName  [12];
	char cmdName   [80];
	sprintf(fileName, "graph%u.dot", count);	
	FILE* handle = fopen(fileName, "w");
	fprintf(handle, "digraph G {\n");
	fprintf(handle, "    size = \"5,7\";");
	printTreeDot(handle, b);
	for (int i = -1; i >= counterZeros; --i)
		fprintf(handle, "    %d [label = \"\", height = 0.01, width = 0.01, style = filled, fillcolor = black];\n", i);
	fprintf(handle, "}\n");
	fclose(handle);
	sprintf(cmdName, "dot -Tps graph%u.dot -o graph%u.ps; gv graph%u.ps &",
			count, count, count);
	system(cmdName);
}

