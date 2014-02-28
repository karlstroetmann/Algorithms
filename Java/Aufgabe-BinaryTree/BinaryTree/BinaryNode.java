/** This class represents an empty node. */
public class BinaryNode<Key extends Comparable, Value> 
	extends Node<Key, Value>
{
	private Key              mKey;
	private Value            mValue;
	private Node<Key, Value> mLeft;
	private Node<Key, Value> mRight;

	public BinaryNode(Key key, Value value) {
		mKey   = key;
		mValue = value;
		mLeft  = new EmptyNode();
		mRight = new EmptyNode();
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

	Triple<Node<Key, Value>, Key, Value> delMin() {
		if (mLeft.isEmpty()) {
			return new Triple(mRight, mKey, mValue);
		} else {
			Triple<Node<Key, Value>, Key, Value> t = mLeft.delMin();
			mLeft = t.getFirst();
			Key   key   = t.getSecond();
			Value value = t.getThird();
			return new Triple(this, key, value);
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
