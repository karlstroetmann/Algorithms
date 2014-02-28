public class BinaryTree<Key extends Comparable, Value> 
	implements MyMap<Key, Value>
{
	// this is the node at the root of the tree
    Node<Key, Value> mRoot;   

	// create an empty tree
    public BinaryTree() {
        mRoot = new EmptyNode();
    }
    
	// search for a key
    public Value find(Key key) {
        return mRoot.find(key);
    }

	// insert a key and a value
    public void insert(Key key, Value value) {
        mRoot = mRoot.insert(key, value);
    }
    
	// delete a key
    public void delete(Key key) {
        mRoot = mRoot.delete(key);
    }
    
	// yields a string representation
    public String toString() {
        return mRoot.toString();
    }
}

    