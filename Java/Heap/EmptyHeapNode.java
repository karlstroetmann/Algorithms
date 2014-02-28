/** This class represents an empty node. */
public class EmptyHeapNode<Key extends Comparable<Key>, Value> 
    extends HeapNode<Key, Value>
{
    public EmptyHeapNode() {
        mCount = 0;
    }

    public Pair<Key, Value> top() {
        return null;
    }
    
    public BinaryHeapNode<Key, Value> insert(Key key, Value value)
    {
        return new BinaryHeapNode<Key, Value>(key, value);
    }
    
    public HeapNode<Key, Value> remove() {
        return this;
    }

    public boolean isEmpty() {
        return true;
    }

    public String toString() {
        return "nil";
    }
}
