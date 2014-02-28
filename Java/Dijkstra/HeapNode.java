import java.util.*;

/** This class represents nodes of a binary tree representing a heap. */
public abstract class HeapNode<Key extends Comparable<Key>, Value>
{
    protected int                        mCount;  // the number of nodes
    protected BinaryHeapNode<Key, Value> mParent; // parent of this node

    // This map stores the nodes associated with the values.
    protected Map<Value, BinaryHeapNode<Key, Value>> mNodeMap;

    public abstract Pair<Key, Value> top();
    
    public abstract BinaryHeapNode<Key, Value> insert(Key key, Value value);
    
    public abstract HeapNode<Key, Value> remove();

    public abstract void change(Key k, Value v);

    public abstract boolean isEmpty();

    public abstract String toString();
}
