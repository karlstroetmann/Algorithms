public class AVLTree<Key extends Comparable<Key>, Value> implements MyMap<Key, Value>
{
	Node<Key, Value> mRoot;  // this is the node at the root of the tree 

	public AVLTree() {
		mRoot = new EmptyNode<Key, Value>();
	}
	
	public Value find(Key key) {
		return mRoot.find(key);
	}

	public void insert(Key key, Value value) {
		mRoot = mRoot.insert(key, value);
	}
	
	public void delete(Key key) {
		mRoot = mRoot.delete(key);
	}

	public String toString() {
		return mRoot.toString();
	}

    // Generate a lopsided AVL tree with height h, where k is the smallest Key.
    public static AVLTree<Integer, Integer> genSkewedTree(int h, int k) {
		AVLTree<Integer, Integer> skewedTree = new AVLTree<Integer, Integer>();
		skewedTree.mRoot = Node.genSkewedTree(h, k);
		return skewedTree;
	}
}

	