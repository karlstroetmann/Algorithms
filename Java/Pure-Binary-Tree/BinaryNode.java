import java.util.*;

/** 
 *  This class represents a binary node. It is made up of a key-value
 *  pair and a left and right subtree.
 *
*/
public class BinaryNode<Key extends Comparable<Key>, Value> 
	extends Node<Key, Value>
{
	private Key              mKey;     // The key stored at the root. 
	private Value            mValue;   // The value attached to this key. 
	private Node<Key, Value> mLeft;    // The left subtree. 
	private Node<Key, Value> mRight;   // The right subtree.

	public BinaryNode(Key key, Value value) {
		mKey   = key;
		mValue = value;
		mLeft  = new EmptyNode<Key, Value>();
		mRight = new EmptyNode<Key, Value>();
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
			return new Triple<Node<Key, Value>, Key, Value>(this, key, value);
		}
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
