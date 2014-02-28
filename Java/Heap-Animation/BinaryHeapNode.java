import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import javax.swing.event.*;

/** This class represents a non-empty node in a binary tree. */
public class BinaryHeapNode<Key extends Comparable<Key>, Value> 
    extends HeapNode<Key, Value>
{
	private Key                  mKey;
	private Value                mValue;
	private HeapNode<Key, Value> mLeft;
	private HeapNode<Key, Value> mRight;

	public BinaryHeapNode(Key key, Value value, 
						  BinaryHeapNode<Key, Value> parent)
	{
		mKey    = key;
		mValue  = value;
		mParent = parent;
		mLeft   = new EmptyHeapNode<Key, Value>(this);
		mRight  = new EmptyHeapNode<Key, Value>(this);
		mCount  = 1;
	}

	// Find the value attached to the key given as argument.
	public Value top() {
		return mValue;
	}
	
	// Insert the given <key, value> pair into the tree.
	public BinaryHeapNode<Key, Value> 
		insert(Key key, Value value, BinaryHeapNode<Key, Value> parent)
	{
		++mCount;
		int cmp = key.compareTo(mKey);
		BinaryHeapNode<Key, Value> node;
		if (cmp < 0) {                            // key < mKey
			if (mLeft.mCount > mRight.mCount) {
				node = mRight.insert(mKey, mValue, this);
				if (mRight.isEmpty()) {
					mRight = node;
				}
			} else {
				node = mLeft.insert(mKey, mValue, this);
				if (mLeft.isEmpty()) {
					mLeft = node;
				}
			}
			mKey   = key;
			mValue = value;
			return this;
		}
		if (mLeft.mCount > mRight.mCount) {
			node = mRight.insert(key, value, this);
			if (mRight.isEmpty()) {
				mRight = node;
			}
		} else {
			node = mLeft.insert(key, value, this);
			if (mLeft.isEmpty()) {
				mLeft = node;
			}
		}
		return node;
	}
	
	// Delete the given key from the tree.
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

	public void change(Key key) {
		mKey = key;
		restoreHeapCondition();
	}

	void restoreHeapCondition() 
	{
		if (mParent == null) {
			return;
		}
		Key   parentKey   = mParent.mKey;
		Value parentValue = mParent.mValue;
		if (parentKey.compareTo(mKey) <= 0) {
			return;
        }
		mParent.mKey   = mKey;
		mParent.mValue = mValue;
		mKey           = parentKey;
		mValue         = parentValue;
		mParent.restoreHeapCondition();
	}

	public boolean isEmpty() {
		return false;
	}

    public String toString() {
        return "(" + mLeft.toString() + ")<" + mKey + "," + mValue + ">(" 
                   + mRight.toString() + ")";
    }

	// All methods below this line are for the graphical animation

    public void draw(double xOffset, double yOffSet) {
        Double width = getWidth();
        mLeft.draw(xOffset, yOffSet + sLength);
        mRight.draw(xOffset + mLeft.getWidth() + sSeparation, yOffSet + sLength);
        mTop = new Point2D.Double(xOffset + 0.5 * width, yOffSet);
    }

    public double getWidth() {
        return mLeft.getWidth() + mRight.getWidth() + sSeparation;
    }
    
    public double getHeight() {
        return Math.max(mLeft.getHeight(), mRight.getHeight()) + sLength;
    }

    public HeapNode<Key, Value> getLeft()  { return mLeft;  }
    public HeapNode<Key, Value> getRight() { return mRight; }

    public Key   getKey()   { return mKey;   }
    public Value getValue() { return mValue; }
    
}

