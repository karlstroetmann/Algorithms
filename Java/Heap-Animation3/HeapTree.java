import java.util.*;

public class HeapTree<Key extends Comparable<Key>, Value>
{
    HeapNode<Key, Value> mRoot;  // this is the node at the root of the tree 

    public HeapTree() {
        Map<Value, BinaryHeapNode<Key, Value>> nodeMap = 
            new HashMap<Value, BinaryHeapNode<Key, Value>>();
        mRoot = new EmptyHeapNode<Key, Value>(null, nodeMap);
    }    
    public Pair<Key, Value> top()            { return mRoot.top();               }
    public void insert(Key key, Value value) { mRoot = mRoot.insert(key, value); }
    public void change(Key key, Value value) { mRoot.change(key, value);         }
    public void remove()                     { mRoot = mRoot.remove();           }
    public String toString()                 { return mRoot.toString();          }
}

    