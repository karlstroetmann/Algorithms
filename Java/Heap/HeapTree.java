public class HeapTree<Key extends Comparable<Key>, Value>
{
    HeapNode<Key, Value> mRoot;  // this is the node at the root of the tree 

    public HeapTree() {
        mRoot = new EmptyHeapNode<Key, Value>();
    }
    
    public Pair<Key, Value> top() {
        return mRoot.top();
    }

    public void insert(Key key, Value value) {
        mRoot = mRoot.insert(key, value);
    }
    
    public void remove() {
        mRoot = mRoot.remove();
    }

    public String toString() {
        return mRoot.toString();
    }
}

    