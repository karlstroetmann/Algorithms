// type of a node
typedef enum { TWO, THREE, FOUR } NodeType;
typedef struct Node* NodePtr;

typedef struct TwoNode*   TwoNodePtr;
typedef struct ThreeNode* ThreeNodePtr;
typedef struct FourNode*  FourNodePtr;

struct TwoNode {
    Key      key;
    Value    val;
    NodePtr  left;
    NodePtr  right;
	unsigned label;  // used for visualization
};

struct ThreeNode {
    Key      key1;
    Value    val1;
    Key      key2;
    Value    val2;
    NodePtr  left;
    NodePtr  middle;
    NodePtr  right;
	unsigned label;  // used for visualization
};

struct FourNode {
    Key      key1;
    Value    val1;
    Key      key2;
    Value    val2;
    Key      key3;
    Value    val3;
    NodePtr  left;
    NodePtr  middleLeft;
    NodePtr  middleRight;
    NodePtr  right;
	unsigned label;  // used for visualization
};

struct Node {
	NodeType type;
	union {
		TwoNodePtr   twoNodePtr;
		ThreeNodePtr threeNodePtr;
		FourNodePtr  fourNodePtr;
	} node;
};

typedef NodePtr Tree;
typedef Tree    Table;

// The code below is for generating the ".dot" files that facilitate a graphical
// representation of a given tree.

void label2Node(TwoNodePtr   t, unsigned* ptrCounter);
void label3Node(ThreeNodePtr t, unsigned* ptrCounter);
void label4Node(FourNodePtr  t, unsigned* ptrCounter);

// This function equips all nodes with a unique label that is later used
// for generating the ".dot" file.

void labelNodes(NodePtr t, unsigned* ptrCounter) 
{
	if (t == 0)
		return;
	unsigned l = *ptrCounter + 1;
	*ptrCounter = l;
	switch (t->type) {
    	case TWO: {
	    	label2Node(t->node.twoNodePtr, ptrCounter);
		    break;
    	}
	    case THREE: {
			label3Node(t->node.threeNodePtr, ptrCounter);
    		break;
	    }
     	case FOUR: {
			label4Node(t->node.fourNodePtr, ptrCounter);
     		break;
	    }
	}
}

void label2Node(TwoNodePtr t, unsigned* ptrCounter)
{
	unsigned l = *ptrCounter + 1;
	*ptrCounter = l;
	t->label = l;	
	labelNodes(t->left,  ptrCounter);
	labelNodes(t->right, ptrCounter);
}

void label3Node(ThreeNodePtr t, unsigned* ptrCounter)
{
	unsigned l = *ptrCounter + 1;
	*ptrCounter = l;
	t->label = l;	
	labelNodes(t->left,   ptrCounter);
	labelNodes(t->middle, ptrCounter);
	labelNodes(t->right,  ptrCounter);
}

void label4Node(FourNodePtr  t, unsigned* ptrCounter)
{
	unsigned l = *ptrCounter + 1;
	*ptrCounter = l;
	t->label = l;	
	labelNodes(t->left,        ptrCounter);
	labelNodes(t->middleLeft,  ptrCounter);
	labelNodes(t->middleRight, ptrCounter);
	labelNodes(t->right,       ptrCounter);
}

void printTwoNode(   FILE* handle, TwoNodePtr   t );
void printThreeNode( FILE* handle, ThreeNodePtr t );
void printFourNode(  FILE* handle, FourNodePtr  t );

unsigned getLabel(NodePtr t) 
{
	assert(t != 0);
	switch (t->type) {
    	case TWO: {
	    	return t->node.twoNodePtr->label;
    	}
	    case THREE: {
		    return t->node.threeNodePtr->label;
	    }
     	case FOUR: {
		    return t->node.fourNodePtr->label;
	    }
	}
	assert(0);
	return 0;
}

void printTreeDot(FILE* handle, Tree t) 
{
	if (t == 0)
		return;
	switch (t->type) {
	case TWO: {
		printTwoNode(handle, t->node.twoNodePtr);
		break;
	}
	case THREE: {
		printThreeNode(handle, t->node.threeNodePtr);
		break;
	}
	case FOUR: {
		printFourNode(handle, t->node.fourNodePtr);
		break;
	}
	}
}

void printTwoNode(FILE* handle, TwoNodePtr t) 
{
	fprintf(handle, "%u [ shape = Mrecord, label = \"%u\" ];\n",
			t->label, t->key);
	if (t->left != 0) {
		fprintf(handle, "%u -> %u [headport = n];\n",
				t->label, getLabel(t->left));
	}
	if (t->right != 0) {
		fprintf(handle, "%u -> %u [headport = n];\n",
				t->label, getLabel(t->right));
	}
	printTreeDot(handle, t->left);
	printTreeDot(handle, t->right);
}

void printThreeNode(FILE* handle, ThreeNodePtr t)
{
	fprintf(handle,
			"%u [ shape = Mrecord, label = \"<f1> %u|<f2> %u\" ];\n",
			t->label, t->key1, t->key2);
	if (t->left != 0) {
		fprintf(handle, "\"%u\":f1 -> %u [tailport = sw, headport = n];\n",
				t->label, getLabel(t->left));
	}
	if (t->middle != 0) {
		fprintf(handle, "%u -> %u        [tailport = s, headport = n];\n",
				t->label, getLabel(t->middle));
	}
	if (t->right != 0) {
		fprintf(handle, "\"%u\":f2 -> %u [tailport = se, headport = n];\n",
				t->label, getLabel(t->right));
	}
	printTreeDot(handle, t->left);
	printTreeDot(handle, t->middle);
	printTreeDot(handle, t->right);
}

void printFourNode(FILE* handle, FourNodePtr t) 
{
	fprintf(handle, 
			"%u [ shape = Mrecord, label = \"<f1> %u|<f2> %u|<f3> %u\" ];\n",
			t->label, t->key1, t->key2, t->key3);
	if (t->left != 0) {
		fprintf(handle, "\"%u\":f1 -> %u [tailport = sw, headport = n];\n",
				t->label, getLabel(t->left));
	}
	if (t->middleLeft != 0) {
		fprintf(handle, "\"%u\":f2 -> %u [tailport = sw, headport = n];\n",
				t->label, getLabel(t->middleLeft));
	}
	if (t->middleRight != 0) {
		fprintf(handle, "\"%u\":f2 -> %u [tailport = se, headport = n];\n",
				t->label, getLabel(t->middleRight));
	}
	if (t->right != 0) {
		fprintf(handle, "\"%u\":f3 -> %u [tailport = se, headport = n];\n",
				t->label, getLabel(t->right));
	}
	printTreeDot(handle, t->left);
	printTreeDot(handle, t->middleLeft);
	printTreeDot(handle, t->middleRight);
	printTreeDot(handle, t->right);
}

// Create a dot file containing the tree.
void printTableDot(Tree t, unsigned count)
{
	unsigned counter = 0;
	char fileName  [12];
	char cmdName   [80];
	sprintf(fileName, "graph%u.dot", count);	
	FILE* handle = fopen(fileName, "w");
	fprintf(handle, "digraph G {\n");
	labelNodes(t, &counter);
	printTreeDot(handle, t);
	fprintf(handle, "}\n");
	fclose(handle);
	sprintf(cmdName, "dot -Tps graph%u.dot -o graph%u.ps; gv graph%u.ps &",
			count, count, count);
	system(cmdName);
}

