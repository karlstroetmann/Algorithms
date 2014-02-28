import java.util.*;

public class BinaryTree<Key extends Comparable<Key>, Value> implements MyMap<Key, Value>
{
	Node<Key, Value> mRoot;  // this is the node at the root of the tree 

	public BinaryTree() {
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

	// Transform the tree into a sorted list.
	public  LinkedList<Key> toList() {
		return mRoot.toList();
	}
}

	