/** This class represents nodes of a binary tree representing a heap. */
public abstract class HeapNode<Key extends Comparable<Key>, Value>
{
	protected int mCount;

	public abstract Pair<Key, Value> top();
	
	public abstract BinaryHeapNode<Key, Value> insert(Key key, Value value);
	
	public abstract HeapNode<Key, Value> remove();

	public abstract boolean isEmpty();

	public abstract String toString();
}
