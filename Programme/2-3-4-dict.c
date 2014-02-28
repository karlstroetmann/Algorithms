// type of a node
typedef enum { TWO, THREE, FOUR } NodeType;
typedef struct Node* NodePtr;

// The structure Node represents either a 2-node, a 3-node, or a 4-node. 
// This is controlled by the flag type.  
//     If type == TWO, the node is a two node and only key1, val1, ptr1, 
// and ptr2 have meaningfull values.  
//    If type == THREE, the node is a 3-node and key1, key2, val1, val2, ptr1,
// ptr2, and ptr3 have meaningfull values.
// Finally, if type == FOUR, the node is a 4-node

struct Node {
	NodeType type;
	bool     alive1;   // indicates whether the first key is deleted
    Key      key1;
    Value    val1;
	bool     alive2;   // indicates whether the first key is deleted
    Key      key2;
    Value    val2;
	bool     alive3;   // indicates whether the first key is deleted
    Key      key3;
    Value    val3;
    NodePtr  ptr1;
    NodePtr  ptr2;
    NodePtr  ptr3;
    NodePtr  ptr4;
	unsigned label;   // used only for printing
};

typedef NodePtr Tree;
typedef Tree    Table;

// The number of valid keys, i.e. keys that are alive.
static unsigned numberValid = 0;

// The number of zombie keys, i.e. keys that have been deleted.
static unsigned numberZombies = 0;

// Search for a given key in the ordered 2-3-4 tree t.
Value* search(Table t, Key key) 
{
	if (t == 0)
		return 0;
	switch (t->type) {
	case TWO: {
		int cmp = compareKey(key, t->key1);
		if (cmp == -1) {
			return search(t->ptr1, key);
		} else if (cmp == 0) {
			if (t->alive1) {
				return &(t->val1);
			} else {
				return 0;
			}
		} 
		assert(cmp == 1);
		return search(t->ptr2, key);
	}
	case THREE: {
		int cmp1 = compareKey(key, t->key1);
		if (cmp1 == -1) {
			return search(t->ptr1, key);
		} else if (cmp1 == 0) {
			if (t->alive1) {
				return &(t->val1);
			} else {
				return 0;
			}
		} 
		assert(cmp1 == 1);
		int cmp2 = compareKey(key, t->key2);
		if (cmp2 == -1) {
			return search(t->ptr2, key);
		} else if (cmp2 == 0) {
			if (t->alive2) {
				return &(t->val2);
			} else {
				return 0;
			}
		}
		assert(cmp2 == 1);
		return search(t->ptr3, key);
	}
	case FOUR: {
		int cmp1 = compareKey(key, t->key1);
		if (cmp1 == -1) {
			return search(t->ptr1, key);
		} else if (cmp1 == 0) {
			if (t->alive1) {
				return &(t->val1);
			} else {
				return 0;
			}
		} 
		assert(cmp1 == 1);
		int cmp2 = compareKey(key, t->key2);
		if (cmp2 == -1) {
			return search(t->ptr2, key);
		} else if (cmp2 == 0) {
			if (t->alive2) {
				return &(t->val2);
			} else {
				return 0;
			}
		}
		assert(cmp2 == 1);
		int cmp3 = compareKey(key, t->key3);
		if (cmp3 == -1) {
			return search(t->ptr3, key);
		} else if (cmp3 == 0) {
			if (t->alive3) {
				return &(t->val3);
			} else {
				return 0;
			}
		}
		assert(cmp3 == 1);
		return search(t->ptr4, key);
	}
	}
	// The statements have been added to avoid a compiler warning.
	assert(0);
	return 0;
}

// Split a given 4-node into three 2-nodes.  The 4-node is disposed.
NodePtr split4Node(NodePtr t)
{
	assert(t->type == FOUR);
	// create the left node
	NodePtr left = malloc( sizeof(struct Node) );
	left->type = TWO;
	left->alive1 = t->alive1;
	left->alive2 = false;
	left->alive3 = false;
	left->key1 = t->key1;
	left->val1 = t->val1;
	left->ptr1 = t->ptr1;
	left->ptr2 = t->ptr2;
	left->ptr3 = 0;
	left->ptr4 = 0;
	// create the right node
	NodePtr right = malloc( sizeof(struct Node) );
	right->type = TWO;
	right->alive1 = t->alive3;
	right->alive2 = false;
	right->alive3 = false;
	right->key1 = t->key3;
	right->val1 = t->val3;
	right->ptr1 = t->ptr3;
	right->ptr2 = t->ptr4;
	right->ptr3 = 0;
	right->ptr4 = 0;
	// create the top node
	NodePtr top = malloc( sizeof(struct Node) );
	top->type = TWO;
	top->alive1 = t->alive2;
	top->alive2 = false;
	top->alive3 = false;
	top->key1 = t->key2;
	top->val1 = t->val2;
	top->ptr1 = left;
	top->ptr2 = right;
	top->ptr3 = 0;
	top->ptr4 = 0;
	free(t);      // the 4-node can be disposed
	return top;
}

// The node n needs to be inserted into the node t as the first child of t.
// The node t must not be a 4-node.  The node n is disposed afterwards.
void moveUp1(NodePtr t, NodePtr n) 
{
	assert(t != 0);
	assert(t->type != FOUR);
	assert(n->type == TWO);
	if (t->type == TWO) {
		t->alive2 = t->alive1;
		t->key2 = t->key1;
		t->val2 = t->val1;
		t->alive1 = n->alive1;
		t->key1 = n->key1;
		t->val1 = n->val1;
		t->ptr3 = t->ptr2;
		t->ptr1 = n->ptr1;
		t->ptr2 = n->ptr2;
		t->type = THREE;
		free(n);
		return;
	} 
	assert(t->type == THREE);
	t->alive3 = t->alive2;
	t->key3 = t->key2;
	t->val3 = t->val2;
	t->alive2 = t->alive1;
	t->key2 = t->key1;
	t->val2 = t->val1;
	t->alive1 = n->alive1;
	t->key1 = n->key1;
	t->val1 = n->val1;
	t->ptr4 = t->ptr3;
	t->ptr3 = t->ptr2;
	t->ptr2 = n->ptr2;
	t->ptr1 = n->ptr1;
	t->type = FOUR;
	free(n);
	return;
}

// The node n needs to be inserted into the node t as the second child of t.
// The node t must not be a 4-node.  The node n is disposed afterwards.
void moveUp2(NodePtr t, NodePtr n) 
{
	assert(t != 0);
	assert(t->type != FOUR);
	assert(n->type == TWO);
	if (t->type == TWO) {
		t->alive2 = n->alive1;
		t->key2 = n->key1;
		t->val2 = n->val1;
		t->ptr2 = n->ptr1;
		t->ptr3 = n->ptr2;
		t->type = THREE;
		free(n);
		return;
	} 
	assert(t->type == THREE);
	t->alive3 = t->alive2;
	t->key3 = t->key2;
	t->val3 = t->val2;
	t->alive2 = n->alive1;
	t->key2 = n->key1;
	t->val2 = n->val1;
	t->ptr4 = t->ptr3;
	t->ptr3 = n->ptr2;
	t->ptr2 = n->ptr1;
	t->type = FOUR;
	free(n);
	return;
}

// The node n needs to be inserted into the node t as the third child of t.
// The node t must be a 3-node.  The node n is disposed afterwards.
void moveUp3(NodePtr t, NodePtr n) 
{
	assert(t != 0);
	assert(t->type == THREE);
	assert(n->type == TWO);
	t->alive3 = n->alive1;
	t->key3 = n->key1;
	t->val3 = n->val1;
	t->ptr4 = n->ptr2;
	t->ptr3 = n->ptr1;
	t->type = FOUR;
	free(n);
	return;
}

// Add a given key value pair to the node p.  It is guaranteed that n is not
// a 4-node. Furthermore, p is a leaf node, i.e. it has no children .
void addKey(Tree p, Key key, Value val) 
{
	if (p->type == TWO) {
		p->alive2 = true;
		int cmp = compareKey(key, p->key1);
		if (cmp == -1) {
			// move the old key to the right
			p->alive2 = p->alive1;
			p->key2 = p->key1;
			p->val2 = p->val1;
			// insert the new key as first key
			p->alive1 = true;
			p->key1 = key;
			p->val1 = val;
		} else {
			assert(cmp == 1);
			// insert the new key as second key
			p->alive2 = true;
			p->key2 = key;
			p->val2 = val;
		}
		p->type = THREE;
		return;
	}
	assert (p->type == THREE);
	p->alive3 = true;
	int cmp1 = compareKey(key, p->key1);
	if (cmp1 == -1) {
		// move the old keys to the right
		p->alive3 = p->alive2;
		p->key3 = p->key2;
		p->val3 = p->val2;
		p->alive2 = p->alive1;
		p->key2 = p->key1;
		p->val2 = p->val1;
		// insert the new key as first key
		p->alive1 = true;
		p->key1 = key;
		p->val1 = val;
	} else {
		assert(cmp1 == 1);
		int cmp2 = compareKey(key, p->key2);
		if (cmp2 == -1) {
			// move the second key right
			p->alive3 = p->alive2;
			p->key3 = p->key2;
			p->val3 = p->val2;
			// insert the new key as second key
			p->alive2 = true;
			p->key2 = key;
			p->val2 = val;
		} else {
			assert(cmp2 == 1);
			// insert the new key at the end
			p->alive3 = true;
			p->key3 = key;
			p->val3 = val;
		}
	}
	p->type = FOUR;
	return;
}

Table insertAux(Table t, Key key, Value val);

// Insert the pair <k,v> into the tree t. The pointer p is the parent of n.
// There are two cases:
//   1. If t is a 4-node, it is first split.  Then <k,v> is inserted in the 
//      resulting tree.  Finally, the top node of this tree is merged with its 
//      parent. 
//   2. Otherwise, i.e. if t is not a 4-node, <k,v> is just inserted in t.
// It is guaranteed that p is not a 4-node.
Tree splitAndInsert(NodePtr p, NodePtr t, Key key, Value val)
{
	// If the parent is a leaf, the key is inserted in the parent.
	if (t == 0) {
		addKey(p, key, val);
		return p;
	}
	// If the root of the subtree where key is to be inserted is
	// a 4-node, split the 4-node first, insert the key into the
	// resulting tree, and finally move the middle key of the resulting
	// tree into the parent node p.
	if (t->type == FOUR) {
		NodePtr n = split4Node(t);
		insertAux(n, key, val);
		if (t == p->ptr1) {
			moveUp1(p, n);
		} else if (t == p->ptr2) {
			moveUp2(p, n);
		} else {
			assert(t == p->ptr3);
			moveUp3(p, n);
		}
		return p;
	}
	insertAux(t, key, val);
	return p;
}

// Insert the pair <key, val> into the tree t, and update the counters.
Table insert(Table t, Key key, Value val) 
{
	// Incrementing numberValid is not always correct: If the key is already
	// contained in the tree t, then the number of valid keys should stay the
	// same.  The easiest thing to fix this problem is to decrement the counter
	// numberValid at the moment when we discover that the key to insert is
	// already present.
	++numberValid;
	return insertAux(t, key, val);
}

// Insert the pair <key, val> into the tree t, without updating any counters.
Tree insertAux(Tree t, Key key, Value val)
{
	if (t == 0) {
		// allocate a new 2 node
		NodePtr node = malloc( sizeof(struct Node) );
		node->type = TWO;
		node->alive1 = true;
		node->alive2 = false;
		node->alive3 = false;
		node->key1 = key;
		node->val1 = val;
		node->ptr1 = 0;
		node->ptr2 = 0;
		node->ptr3 = 0;
		node->ptr4 = 0;
		return node;
	}
	switch (t->type) {
	case TWO: {
		int cmp = compareKey(key, t->key1);
		if (cmp == -1) {
			t = splitAndInsert(t, t->ptr1, key, val);
		} else if (cmp == 0) {
			// We have already incremented numberValid, we need to correct 
			// this now, compare the comment in insert().
			if (t->alive1) {
				--numberValid;
			}
			t->alive1 = true;
			t->val1 = val;
		} else {
			assert(cmp == 1);
			t = splitAndInsert(t, t->ptr2, key, val);
		}
		return t;
	}
	case THREE: {
		int cmp1 = compareKey(key, t->key1);
		if (cmp1 == -1) {
			t = splitAndInsert(t, t->ptr1, key, val);
		} else if (cmp1 == 0) {
			// We have already incremented numberValid, we need to correct 
			// this now, compare the comment in insert().
			if (t->alive1){
				--numberValid;
			}
			t->alive1 = true;
			t->val1 = val;
		} else {
			assert(cmp1 == 1);
			int cmp2 = compareKey(key, t->key2);
			if (cmp2 == -1) {
				t = splitAndInsert(t, t->ptr2, key, val);
			} else if (cmp2 == 0) {
				// We have already incremented numberValid, we need to correct 
				// this now, compare the comment in insert().
				if (t->alive2) {
					--numberValid;
				}
				t->alive2 = true;
				t->val2 = val;
			} else {
				assert(cmp2 == 1);
				t = splitAndInsert(t, t->ptr3, key, val);
			}
		}
		return t;
	}
	case FOUR: {
		NodePtr n = split4Node(t);
		return insertAux(n, key, val);
	}
	}
	assert(0);
	return 0;
}


// Delete a given key in the ordered 2-3-4 tree t.  We just 
Tree deleteAux(Tree t, Key key) 
{
	if (t == 0) {
		// In this case, the key to delete does not occur in the tree t.
		// Therefore, we need to decrement numberZombies, since we have already
        // incremented it in the function delete(). Similarly, we need to 
		// increment numberValid, since we have decremented it.
		++numberValid;
		--numberZombies;
		return 0;
	}
	switch (t->type) {
	case TWO: {
		int cmp = compareKey(key, t->key1);
		if (cmp == -1) {
			deleteAux(t->ptr1, key);
			return t;
		} else if (cmp == 0) {
			if (!t->alive1) {
				// the key is already a zombie 
				++numberValid;
				--numberZombies;
			}
			t->alive1 = false;
			return t;
		} 
		assert(cmp == 1);
		deleteAux(t->ptr2, key);
		return t;
	}
	case THREE: {
		int cmp1 = compareKey(key, t->key1);
		if (cmp1 == -1) {
			deleteAux(t->ptr1, key);
			return t;
		} else if (cmp1 == 0) {
			if (!t->alive1) {
				// the key is already a zombie 
				++numberValid;
				--numberZombies;
			}
			t->alive1 = false;
			return t;
		} 
		assert(cmp1 == 1);
		int cmp2 = compareKey(key, t->key2);
		if (cmp2 == -1) {
			deleteAux(t->ptr2, key);
			return t;
		} else if (cmp2 == 0) {
			if (!t->alive2) {
				// the key is already a zombie 
				++numberValid;
				--numberZombies;
			}
			t->alive2 = false;
			return t;
		}
		assert(cmp2 == 1);
		deleteAux(t->ptr3, key);
		return t;
	}
	case FOUR: {
		int cmp1 = compareKey(key, t->key1);
		if (cmp1 == -1) {
			deleteAux(t->ptr1, key);
			return t;
		} else if (cmp1 == 0) {
			if (!t->alive1) {
				// the key is already a zombie 
				++numberValid;
				--numberZombies;
			}
			t->alive1 = false;
			return t;
		} 
		assert(cmp1 == 1);
		int cmp2 = compareKey(key, t->key2);
		if (cmp2 == -1) {
			deleteAux(t->ptr2, key);
			return t;
		} else if (cmp2 == 0) {
			if (!t->alive2) {
				// the key is already a zombie 
				++numberValid;
				--numberZombies;
			}
			t->alive2 = false;
			return t;
		}
		assert(cmp2 == 1);
		int cmp3 = compareKey(key, t->key3);
		if (cmp3 == -1) {
			deleteAux(t->ptr3, key);
			return t;
		} else if (cmp3 == 0) {
			if (!t->alive3) {
				// the key is already a zombie 
				++numberValid;
				--numberZombies;
			}
			t->alive3 = false;
			return t;
		}
		assert(cmp3 == 1);
		deleteAux(t->ptr4, key);
		return t;
	}
	}
	// The statements have been added to avoid a compiler warning.
	assert(0);
	return 0;
}

// Collect all the keys and the values of the tree t and store them
// into keyArray and valArray. idx is used an index into these arrays.
void collectKeys(Tree t, unsigned* idx, Key keyArray[], Value valArray[]) 
{
	if (t == 0)
		return;
	switch (t->type) {
	case TWO: {
		collectKeys(t->ptr1, idx, keyArray, valArray);
		if (t->alive1) {
			keyArray[*idx] = t->key1;
			valArray[*idx] = t->val1;
			*idx = *idx + 1;
		}
		collectKeys(t->ptr2, idx, keyArray, valArray);
		return;
	}
	case THREE: {
		collectKeys(t->ptr1, idx, keyArray, valArray);
		if (t->alive1) {
			keyArray[*idx] = t->key1;
			valArray[*idx] = t->val1;
			*idx = *idx + 1;
		}
		collectKeys(t->ptr2, idx, keyArray, valArray);
		if (t->alive2) {
			keyArray[*idx] = t->key2;
			valArray[*idx] = t->val2;
			*idx = *idx + 1;
		}
		collectKeys(t->ptr3, idx, keyArray, valArray);
		return;
	}
	case FOUR: {
		collectKeys(t->ptr1, idx, keyArray, valArray);
		if (t->alive1) {
			keyArray[*idx] = t->key1;
			valArray[*idx] = t->val1;
			*idx = *idx + 1;
		}
		collectKeys(t->ptr2, idx, keyArray, valArray);
		if (t->alive2) {
			keyArray[*idx] = t->key2;
			valArray[*idx] = t->val2;
			*idx = *idx + 1;
		}
		collectKeys(t->ptr3, idx, keyArray, valArray);
		if (t->alive3) {
			keyArray[*idx] = t->key3;
			valArray[*idx] = t->val3;
			*idx = *idx + 1;
		}
		collectKeys(t->ptr4, idx, keyArray, valArray);
		return;
	}
	}	
}

// This function gets an array of keys and an array of associated values.
// The array of keys is assumed to be sorted.  The number of
// elements of these arrays that shall be used is given as the last argument. 
// The function builds a 2-3-4 tree from the <key, value> pairs in keyArray
// and valArray.

Tree array2tree(Key keyArray[], Value valArray[], unsigned length)
{
	if (length == 0)
		return 0;
	// create a 2--node
	if (length == 1) {
		NodePtr root = malloc( sizeof(struct Node) );
		root->type = TWO;
		root->alive1 = true;
		root->key1 = keyArray[0];
		root->val1 = valArray[0];
		root->alive2 = false;
		root->alive3 = false;
		root->ptr1 = 0;
		root->ptr2 = 0;
		root->ptr3 = 0;
		root->ptr4 = 0;
		return root;
	}
	// create a 3--node
	if (length == 2) {
		NodePtr root = malloc( sizeof(struct Node) );
		root->type = THREE;
		root->alive1 = true;
		root->key1 = keyArray[0];
		root->val1 = valArray[0];
		root->alive2 = true;
		root->key2 = keyArray[1];
		root->val2 = valArray[1];
		root->alive3 = false;
		root->ptr1 = 0;
		root->ptr2 = 0;
		root->ptr3 = 0;
		root->ptr4 = 0;
		return root;
	}
	if (length % 2 == 1) {
		// In the first case, length has the form length = 2 * m + 1.  So 
		// we split the arrays as follows:
		//  1. Half:        indices 0, 1, 2, ..., m - 1
		//  Middle element: index   m
		//  2. Half:        indices m + 1, m + 2, ..., m + m - 1
		// Therefore, the first and second half each contain m elements.
		unsigned m = length / 2;
		Tree  left  = array2tree(keyArray, valArray, m);
		Tree  right = array2tree(keyArray + m + 1, valArray + m + 1, m);
		NodePtr root = malloc( sizeof(struct Node) );
		root->type = TWO;
		root->alive1 = true;
		root->key1 = keyArray[m];
		root->val1 = valArray[m];
		root->alive2 = false;
		root->alive3 = false;
		root->ptr1 = left;
		root->ptr2 = right;
		root->ptr3 = 0;
		root->ptr4 = 0;
		return root;
	} else {
		// In the second case, length has the form length = 2 * m + 2.  So 
		// we split the arrays as follows:
		//  1. Half:         indices 0, 1, 2, ..., m - 1
		//  Middle element:  index   m
		//  Surplus element: index   m + 1
		//  2. Half:         indices m + 2, m + 3, ..., m + m + 1
		// Therefore, the first and second half each contain m elements.
		unsigned m = length / 2 - 1;
		Tree  left  = array2tree(keyArray, valArray, m);
		Tree  right = array2tree(keyArray + m + 2, valArray + m + 2, m);
		NodePtr root = malloc( sizeof(struct Node) );
		root->type = TWO;
		root->alive1 = true;
		root->key1 = keyArray[m];
		root->val1 = valArray[m];
		root->alive2 = false;
		root->alive3 = false;
		root->ptr1 = left;
		root->ptr2 = right;
		root->ptr3 = 0;
		root->ptr4 = 0;
		// We must not forget to add the surplus element.
		return insert(root, keyArray[m + 1], valArray[m + 1]);
	}
}

// This function frees all nodes of a given tree.
void freeAll(Tree t) 
{
	if (t == 0)
		return;
	freeAll(t->ptr1);
	freeAll(t->ptr2);
	freeAll(t->ptr3);
	freeAll(t->ptr4);
	free(t);
}

// Get rid of the zombies.
Tree rebuildTree(Tree t)
{
	Key      keyArray[numberValid];
	Value    valArray[numberValid];
	unsigned idx = 0;
	collectKeys(t, &idx, keyArray, valArray);
	numberZombies = 0;
	freeAll(t);
	return array2tree(keyArray, valArray, numberValid);
}

Table delete(Table t, Key key) 
{
	// Decrementing numberValid and incrementing numberZombies is not always
    // correct: If the key does not occur in the tree t, then both the number
	// of valid keys and the number of zombie keys should stay the same.  
	// The easiest way to fix this problem is to increment the counter 
	// numberValid and decrement the counter numberZombies at the moment when
	// we discover that the key to delete does not occur in the tree.
	--numberValid;
	++numberZombies;
	if (numberZombies > numberValid) {
		Tree t1 = deleteAux(t, key);
		return rebuildTree(t1);
	}
	return deleteAux(t, key);
}

// The code below is for generating the ".dot" files that facilitate a graphical
// representation of a given tree.

// This function equips all nodes with a unique label that is later used
// for generating the ".dot" file.
void labelNodes(Tree t, unsigned* ptrCounter) 
{
	if (t == 0)
		return;
	*ptrCounter = *ptrCounter + 1;
	t->label = *ptrCounter;
	labelNodes(t->ptr1, ptrCounter);
	labelNodes(t->ptr2, ptrCounter);
	labelNodes(t->ptr3, ptrCounter);
	labelNodes(t->ptr4, ptrCounter);
}

void printTreeDot(FILE* handle, Tree t) 
{
	if (t == 0)
		return;
	switch (t->type) {
	case TWO: {
		if (t->alive1) {
			fprintf(handle, "%u [ shape = Mrecord, label = \"%u\" ];\n",
					t->label, t->key1);
		} else {
			fprintf(handle, "%u [ shape = Mrecord, label = \"(%u)\" ];\n",
					t->label, t->key1);
		}
		if (t->ptr1 != 0) {
			fprintf(handle, "%u -> %u [headport = n];\n",
					t->label, t->ptr1->label);
		}
		if (t->ptr2 != 0) {
			fprintf(handle, "%u -> %u [headport = n];\n",
					t->label, t->ptr2->label);
		}
		break;
	}
	case THREE: {
		if (t->alive1 && t->alive2) {
			fprintf(handle,
					"%u [ shape = Mrecord, label = \"<f1> %u|<f2> %u\" ];\n",
					t->label, t->key1, t->key2);
		} else if (!t->alive1 && t->alive2) {
			fprintf(handle,
					"%u [ shape = Mrecord, label = \"<f1> (%u)|<f2> %u\" ];\n",
					t->label, t->key1, t->key2);
		} else if (t->alive1 && !t->alive2) {
			fprintf(handle,
					"%u [ shape = Mrecord, label = \"<f1> %u|<f2> (%u)\" ];\n",
					t->label, t->key1, t->key2);
		} else {
			fprintf(handle,
					"%u [ shape = Mrecord, label = \"<f1> (%u)|<f2> (%u)\" ];\n",
					t->label, t->key1, t->key2);
		}
		if (t->ptr1 != 0) {
			fprintf(handle, "\"%u\":f1 -> %u [tailport = sw, headport = n];\n",
					t->label, t->ptr1->label);
		}
		if (t->ptr2 != 0) {
			fprintf(handle, "%u -> %u        [tailport = s, headport = n];\n",
					t->label, t->ptr2->label);
		}
		if (t->ptr3 != 0) {
			fprintf(handle, "\"%u\":f2 -> %u [tailport = se, headport = n];\n",
					t->label, t->ptr3->label);
		}
		break;
	}
	case FOUR: {
		if (t->alive1 && t->alive2 && t->alive3) {
			fprintf(handle, 
					"%u [ shape = Mrecord, label = \"<f1> %u|<f2> %u|<f3> %u\" ];\n",
					t->label, t->key1, t->key2, t->key3);
		} else if (!t->alive1 && t->alive2 && t->alive3) {
			fprintf(handle, 
					"%u [ shape = Mrecord, label = \"<f1> (%u)|<f2> %u|<f3> %u\" ];\n",
					t->label, t->key1, t->key2, t->key3);
		} else if (t->alive1 && !t->alive2 && t->alive3) {
			fprintf(handle, 
					"%u [ shape = Mrecord, label = \"<f1> %u|<f2> (%u)|<f3> %u\" ];\n",
					t->label, t->key1, t->key2, t->key3);
		} else if (t->alive1 && t->alive2 && !t->alive3) {
			fprintf(handle, 
					"%u [ shape = Mrecord, label = \"<f1> %u|<f2> %u|<f3> (%u)\" ];\n",
					t->label, t->key1, t->key2, t->key3);
		} else if (t->alive1 && !t->alive2 && !t->alive3) {
			fprintf(handle, 
					"%u [ shape = Mrecord, label = \"<f1> %u|<f2> (%u)|<f3> (%u)\" ];\n",
					t->label, t->key1, t->key2, t->key3);
		} else if (!t->alive1 && t->alive2 && !t->alive3) {
			fprintf(handle, 
					"%u [ shape = Mrecord, label = \"<f1> (%u)|<f2> %u|<f3> (%u)\" ];\n",
					t->label, t->key1, t->key2, t->key3);
		} else if (!t->alive1 && !t->alive2 && t->alive3) {
			fprintf(handle, 
					"%u [ shape = Mrecord, label = \"<f1> (%u)|<f2> (%u)|<f3> %u\" ];\n",
					t->label, t->key1, t->key2, t->key3);
		} else {
			fprintf(handle, 
					"%u [ shape = Mrecord, label = \"<f1> (%u)|<f2> (%u)|<f3> (%u)\" ];\n",
					t->label, t->key1, t->key2, t->key3);
		}
		if (t->ptr1 != 0) {
			fprintf(handle, "\"%u\":f1 -> %u [tailport = sw, headport = n];\n",
					t->label, t->ptr1->label);
		}
		if (t->ptr2 != 0) {
			fprintf(handle, "\"%u\":f2 -> %u [tailport = sw, headport = n];\n",
					t->label, t->ptr2->label);
		}
		if (t->ptr3 != 0) {
			fprintf(handle, "\"%u\":f2 -> %u [tailport = se, headport = n];\n",
					t->label, t->ptr3->label);
		}
		if (t->ptr4 != 0) {
			fprintf(handle, "\"%u\":f3 -> %u [tailport = se, headport = n];\n",
					t->label, t->ptr4->label);
		}
		break;
	}
	}
	printTreeDot(handle, t->ptr1);
	printTreeDot(handle, t->ptr2);
	printTreeDot(handle, t->ptr3);
	printTreeDot(handle, t->ptr4);
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

