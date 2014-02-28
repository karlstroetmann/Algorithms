import java.util.*;

/** \class BinaryNode 
 *  This class represents a binary node. It is made up of a key-value
 *  pair and a left and right subtree.
 *
 */
public class BinaryNode<Key extends Comparable<Key>, Value> 
    extends Node<Key, Value>
{
    private Key              mKey;     /**< The key stored at the root. */
    private Value            mValue;   /**< The value attached to this key. */
    private Node<Key, Value> mLeft;    /**< The left subtree. */
    private Node<Key, Value> mRight;   /**< The right subtree. */

    public BinaryNode(Key key, Value value) {
        mKey    = key;
        mValue  = value;
        mLeft   = new EmptyNode<Key, Value>();
        mRight  = new EmptyNode<Key, Value>();
        mHeight = 1;
    }

    public BinaryNode(Key key, Value value, Node<Key, Value> left, 
                                            Node<Key, Value> right) {
        mKey    = key;
        mValue  = value;
        mLeft   = left;
        mRight  = right;
        mHeight = 1 + Math.max(mLeft.mHeight, mRight.mHeight);
    }
    
    // Find the value attached to the key given as argument.
    public Value find(Key key) {
        int cmp = key.compareTo(mKey);
        if (cmp < 0) {                // key < mKey
            return mLeft.find(key);
        } else if (cmp > 0) {         // key > mKey
            return mRight.find(key);
        } else {                      // key == mKey
            return mValue;
        }            
    }
    
    // Insert the given <key, value> pair into the tree.
    public Node<Key, Value> insert(Key key, Value value) {
        int cmp = key.compareTo(mKey);
        if (cmp < 0) {                        // key < mKey
            mLeft = mLeft.insert(key, value);
        } else if (cmp > 0) {                 // key > mKey
            mRight = mRight.insert(key, value);
        } else {                              // key == mKey
            mValue = value;
        }
        restore();
        return this;
    }
    
    // Delete the given key from the tree.
    public Node<Key, Value> delete(Key key) {
        int cmp = key.compareTo(mKey);
        if (cmp == 0) {
            if (mLeft.isEmpty()) {
                return mRight;
            } 
            if (mRight.isEmpty()) {
                return mLeft;
            }
            Triple<Node<Key, Value>, Key, Value> triple = mRight.delMin();
            mRight = triple.getFirst();
            mKey   = triple.getSecond();
            mValue = triple.getThird();
        }
        if (cmp < 0) {
            mLeft = mLeft.delete(key);
        }
        if (cmp > 0) {
            mRight = mRight.delete(key);
        }
        restore();
        return this;
    }

    public boolean isEmpty() {
        return false;
    }

    public LinkedList<Key> toList() {
        LinkedList<Key> result = mLeft.toList();
        result.addLast(mKey);
        result.addAll(mRight.toList());
        return result;
    }

    Triple<Node<Key, Value>, Key, Value> delMin() {
        if (mLeft.isEmpty()) {
            return new Triple<Node<Key, Value>, Key, Value>(mRight, mKey, mValue);
        } else {
            Triple<Node<Key, Value>, Key, Value> t = mLeft.delMin();
                  mLeft = t.getFirst();
            Key   key   = t.getSecond();
            Value value = t.getThird();
            restore();
            return new Triple<Node<Key, Value>, Key, Value>(this, key, value);
        }
    }

    void restore() {
        // if the tree is already balanced, then we only have to recalcuate
    	// the height of the tree
        if (Math.abs(mLeft.mHeight - mRight.mHeight) <= 1) {
            restoreHeight();
            return;
        }
        if (mLeft.mHeight > mRight.mHeight) {
            Key   k1 = mKey;
            Value v1 = mValue;
            BinaryNode<Key, Value> l1 = (BinaryNode<Key, Value>) mLeft;
            Node<Key, Value>  r1 = mRight;
            Key   k2 = l1.mKey;
            Value v2 = l1.mValue;
            Node<Key, Value> l2 = l1.mLeft;
            Node<Key, Value> r2 = l1.mRight;
            if (l2.mHeight >= r2.mHeight) {
                mKey   = k2;
                mValue = v2;
                mLeft  = l2;
                mRight = new BinaryNode<Key, Value>(k1, v1, r2, r1);
            } else {
                BinaryNode<Key, Value> rb2 = (BinaryNode<Key, Value>) r2;
                Key   k3 = rb2.mKey;
                Value v3 = rb2.mValue;
                Node<Key, Value>  l3 = rb2.mLeft;
                Node<Key, Value>  r3 = rb2.mRight;
                mKey   = k3;
                mValue = v3;
                mLeft  = new BinaryNode<Key, Value>(k2, v2, l2, l3);
                mRight = new BinaryNode<Key, Value>(k1, v1, r3, r1);
            }
        }
        if (mRight.mHeight > mLeft.mHeight) {
            Key        k1 = mKey;
            Value      v1 = mValue;
            Node<Key, Value>       l1 = mLeft;
            BinaryNode<Key, Value> r1 = (BinaryNode<Key, Value>) mRight;
            Key        k2 = r1.mKey;
            Value      v2 = r1.mValue;
            Node<Key, Value>       l2 = r1.mLeft;
            Node<Key, Value>       r2 = r1.mRight;
            if (r2.mHeight >= l2.mHeight) {
                mKey   = k2;
                mValue = v2;
                mLeft  = new BinaryNode<Key, Value>(k1, v1, l1, l2);
                mRight = r2;
            } else {
                BinaryNode<Key, Value> lb2 = (BinaryNode<Key, Value>) l2;
                Key        k3 = lb2.mKey;
                Value      v3 = lb2.mValue;
                Node<Key, Value>       l3 = lb2.mLeft;
                Node<Key, Value>       r3 = lb2.mRight;
                mKey   = k3;
                mValue = v3;
                mLeft  = new BinaryNode<Key, Value>(k1, v1, l1, l3);
                mRight = new BinaryNode<Key, Value>(k2, v2, r3, r2);
            }
        }
        restoreHeight();
    }

    void restoreHeight() {
        mHeight = 1 + Math.max(mLeft.mHeight, mRight.mHeight);
    }
}

// This class represent the triple <mFirst, mSecond, mThird>
class Triple<First, Second, Third> 
{
    First  mFirst;
    Second mSecond;
    Third  mThird;
    
    Triple(First first, Second second, Third third) {
        mFirst  = first;
        mSecond = second;
        mThird  = third;
    }

    First  getFirst()  { return mFirst;  }
    Second getSecond() { return mSecond; }
    Third  getThird()  { return mThird;  }
}
