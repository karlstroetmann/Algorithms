import java.util.*;

/** This class represents a non-empty node in a binary tree. */
public class BinaryHeapNode<Key extends Comparable<Key>, Value> 
    extends HeapNode<Key, Value>
{
    private Key                  mKey;    // The priority associated with the value.
    private Value                mValue;  // The value.
    private HeapNode<Key, Value> mLeft;   // The root of the left subtree.
    private HeapNode<Key, Value> mRight;  // The root of the right subtree.

    public BinaryHeapNode(Key key, Value value) {
        mKey    = key;
        mValue  = value;
        mLeft   = new EmptyHeapNode<Key, Value>();
        mRight  = new EmptyHeapNode<Key, Value>();
        mCount  = 1;
    }

    public Pair<Key, Value> top() {
        return new Pair<Key, Value>(mKey, mValue);
    }
    
    public BinaryHeapNode<Key, Value> insert(Key key, Value value)
    {
        ++mCount;
        int cmp = key.compareTo(mKey);
        if (cmp < 0) {                         
            if (mLeft.mCount > mRight.mCount) {
                mRight = mRight.insert(mKey, mValue);
            } else {
                mLeft = mLeft.insert(mKey, mValue);
            }
            mKey   = key;
            mValue = value;
        } else {
            if (mLeft.mCount > mRight.mCount) {
                mRight = mRight.insert(key, value);
            } else {
                mLeft = mLeft.insert(key, value);
            }
        }
        return this;
    }
    
    public HeapNode<Key, Value> remove() {
        --mCount;
        if (mLeft.isEmpty()) {
            return mRight;
        } 
        if (mRight.isEmpty()) {
            return mLeft;
        }
        BinaryHeapNode<Key, Value> left  = (BinaryHeapNode<Key, Value>) mLeft;
        BinaryHeapNode<Key, Value> right = (BinaryHeapNode<Key, Value>) mRight;
        Key leftKey  = left .mKey;
        Key rightKey = right.mKey;
        if (leftKey.compareTo(rightKey) < 0) {
            mKey   = left.mKey;
            mValue = left.mValue;
            mLeft  = mLeft.remove();
        } else {
            mKey   = right.mKey;
            mValue = right.mValue;
            mRight = mRight.remove();
        }
        return this;
    }

    public boolean isEmpty() {
        return false;
    }

    public String toString() {
        return "(" + mLeft.toString() + ")<" + mKey + "," + mValue + ">(" 
                   + mRight.toString() + ")";
    }    
}

