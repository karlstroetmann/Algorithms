public class BinaryTree<Key extends Comparable, Value> implements MyMap<Key, Value>
{
	Node<Key, Value> mRoot;  // this is the node at the root of the tree 

	public BinaryTree() {
		mRoot = new EmptyNode();
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
}

	