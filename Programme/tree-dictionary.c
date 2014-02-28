typedef struct BinNode* BinNodePtr;

struct BinNode {
    Key        key;
    Value      val;
    BinNodePtr left;
    BinNodePtr right;
};

typedef BinNodePtr BinTree;
typedef BinTree    Table;

bool ordered(BinTree t);

// A constructor for list nodes.
Table insert(Table t, Key key, Value val)
{
	if (t == 0) {
		BinTree nodePtr = malloc( sizeof(struct BinNode) );
		nodePtr->key    = key;
		nodePtr->val    = val;
		nodePtr->left   = 0;
		nodePtr->right  = 0;
		return nodePtr;
	}
	int cmp = compareKey(key, t->key);
	if (cmp == -1) {
		t->left = insert(t->left, key, val);
	} else if (cmp == 1) {
		t->right = insert(t->right, key, val);
	} else {
		t->val = val;
	}
	assert( ordered(t) );
	return t;
}

// Search for a given key in the ordered binary tree t.
Value* search(Table t, Key key) 
{
	if (t == 0)
		return 0;
	int cmp = compareKey(key, t->key);
	if (cmp == -1)
		return search(t->left, key);
	if (cmp == 1)
		return search(t->right, key);
	// Now the only possibility that is left is cmp == 0, 
    // that is key == t->key.
	return &(t->val);
}

BinNodePtr findRightSucc(Table t) 
{
	if (t->left == 0)
		return t;
	return findRightSucc(t->left);
}

// Delete a given key from the table t.  We assume that the key appears
// in t.
Table delete(Table t, Key key) 
{
	assert(t != 0);
	int cmp = compareKey(key, t->key);
	if (cmp == -1)
		t->left = delete(t->left, key);
	if (cmp == 1)
		t->right = delete(t->right, key);
	if (cmp != 0)
		return t;
	// If we ever get here, we must have key == t->key.
	if (t->left == 0 && t->right == 0) {
		free(t);
		return 0;
	}
	if (t->left == 0) {
		Table res = t->right;
		free(t);
		return res;
	}
	if (t->right == 0) {
		Table res = t->left;
		free(t);
		return res;
	}
	BinNodePtr ptr = findRightSucc(t->right);
	t->key = ptr->key;
	t->val = ptr->val;
	t->right = delete(t->right, ptr->key);
	assert( ordered(t) );
	return t;	
}

// This function frees all nodes of a given tree.
void deleteAll(BinTree t) 
{
	if (t == 0)
		return;
	deleteAll(t->left);
	deleteAll(t->right);
	free(t);
}

// The code below is only needed for visualzation and testing.

// This function returns true iff the Key k is less than every key 
// that appears in the binary tree b.
bool less(Key k, BinTree b) 
{
	if (b == 0)
		return true;
	int cmp = compareKey(k, b->key);
	return cmp == -1 && less(k, b->left) && less(k, b->right);
}

// This function returns true iff the Key k is greater than every key 
// that appears in the binary tree b.
bool greater(Key k, BinTree b) 
{
	if (b == 0)
		return true;
	int cmp = compareKey(k, b->key);
	return cmp == 1 && greater(k, b->left) && greater(k, b->right);
}

// This function returns true if the binary tree t is ordered.
bool ordered(BinTree t) 
{
	if (t == 0)
		return true;
	return 
		greater(t->key, t->left) && less(t->key, t->right) &&
		ordered(t->left)         && ordered(t->right);
}

// This function computes the length of the longest path in the tree
// leading from a root to a leaf.
unsigned height(BinTree t)
{
	if (t == 0)
		return 0;
	return 1 + max( height(t->left), height(t->right) );
}

// This function computes the number of nodes in a given tree.
unsigned nodeCount(BinTree t)
{
	if (t == 0)
		return 0;
	return 1 + nodeCount(t->left) + nodeCount(t->right);
}

// This function computes the sum of the length of all pathes from the root
// to the nodes in a given binary tree.
unsigned totalLength(BinTree t) 
{
	if (t == 0)
		return 0;
	return 
		nodeCount(t->left)  + totalLength(t->left) +
		nodeCount(t->right) + totalLength(t->right);
}

// Print the given tree: First the left branch is printed,
// next the top node is printed, then the right branch is printed.
// The top node is indented by indent spaces, while left and
// right subtree are indented by indented + 4 spaces.
void printTree(BinTree b, unsigned indent)
{
	if (b == 0)
		return;
	printTree(b->left, indent + 4);
	for (unsigned i = 0; i < indent; ++i)
		printf(" ");
	printKey(b->key);
	printf(": ");
	printVal(b->val);
	printf("\n");
	printTree(b->right, indent + 4);	
}

void printTable(Table t) 
{
	printTree(t, 0);
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

// Create a dot file containing the tree.
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

