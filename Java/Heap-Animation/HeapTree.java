public class HeapTree<Key extends Comparable<Key>, Value>
{
	HeapNode<Key, Value> mRoot;  // this is the node at the root of the tree 

	public HeapTree() {
		mRoot = new EmptyHeapNode<Key, Value>(null);
	}
	
	public Value top() {
		return mRoot.top();
	}

	public BinaryHeapNode<Key, Value> insert(Key key, Value value) {
		BinaryHeapNode<Key, Value> node = mRoot.insert(key, value, null);
		if (mRoot.isEmpty()) {
			mRoot = node;
		}
		return node;
	}
	
	public void remove() {
		mRoot = mRoot.remove();
	}

	public String toString() {
		return mRoot.toString();
	}
}

	