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

    public BinaryHeapNode(Key                                    key, 
                          Value                                  value, 
                          BinaryHeapNode<Key, Value>             parent,
                          Map<Value, BinaryHeapNode<Key, Value>> nodeMap)
    {
        mKey     = key;
        mValue   = value;
        mParent  = parent;
        mNodeMap = nodeMap;
        mLeft    = new EmptyHeapNode<Key, Value>(this, nodeMap);
        mRight   = new EmptyHeapNode<Key, Value>(this, nodeMap);
        mCount   = 1;
    }

    // Find the value attached to the key given as argument.
    public Pair<Key, Value> top() {
        return new Pair<Key, Value>(mKey, mValue);
    }
    
    // Insert the given <key, value> pair into the tree.
    public BinaryHeapNode<Key, Value> insert(Key key, Value value) {
        ++mCount;
        int cmp = key.compareTo(mKey);
        if (cmp < 0) {
            mNodeMap.remove(mValue);
            if (mLeft.mCount > mRight.mCount) {
                mRight = mRight.insert(mKey, mValue);
            } else {
                mLeft  = mLeft .insert(mKey, mValue);
            }
            mKey   = key;
            mValue = value;
            mNodeMap.put(value, this);
        } else {
            if (mLeft.mCount > mRight.mCount) {
                mRight = mRight.insert(key, value);
            } else {
                mLeft  = mLeft .insert(key, value);
            }
        }
        return this;
    }
    
    // Delete the given key from the tree.
    public HeapNode<Key, Value> remove() {
        mNodeMap.remove(mValue);
        if (mLeft.isEmpty()) {
            mRight.mParent = mParent;
            return mRight;
        } 
        if (mRight.isEmpty()) {
            mLeft.mParent = mParent;
            return mLeft;
        }
        --mCount;
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
        mNodeMap.put(mValue, this);
        repair();
        return this;
    }

    private void repair() {
        if (Math.abs(mLeft.mCount - mRight.mCount) <= 1) {
            return;
        }
        if (mLeft.mCount == mRight.mCount + 2) {
            BinaryHeapNode<Key, Value> left  = (BinaryHeapNode<Key, Value>) mLeft;
            Key   key   = left.mKey;
            Value value = left.mValue;
            mLeft  = mLeft .remove();
            mRight = mRight.insert(key, value);
            return;
        } else if (mRight.mCount == mLeft.mCount + 2) {
            BinaryHeapNode<Key, Value> right = (BinaryHeapNode<Key, Value>) mRight;
            Key   key   = right.mKey;
            Value value = right.mValue;
            mRight = mRight.remove();
            mLeft  = mLeft .insert(key, value);
            return;
        }
        assert false : "*** Internal error in repair() ***";
    }       

    public void change(Key key, Value value) {
        BinaryHeapNode<Key, Value> node = mNodeMap.get(value);
        node.mKey = key;
        assert node.mValue == value : "*** Internal error in change() ***";
        node.upHeap();
    }

    private void upHeap() 
    {
        if (mParent == null) {
            return;
        }
        Key   parentKey   = mParent.mKey;
        Value parentValue = mParent.mValue;
        if (parentKey.compareTo(mKey) <= 0) {
            return;
        }
        mNodeMap.remove(mValue);
        mNodeMap.remove(mParent.mValue);
        mNodeMap.put(mValue, mParent);
        mNodeMap.put(parentValue, this);
        mParent.mKey   = mKey;
        mParent.mValue = mValue;
        mKey           = parentKey;
        mValue         = parentValue;
        mParent.upHeap();
    }

    public boolean isEmpty() {
        return false;
    }

    public String toString() {
        return "node(" + mKey + ", " + mValue + ", " 
                       + mLeft + ", " + mRight + ")";
    }

    // All methods below this line are for the graphical animation
    public void draw(double xOffset, double yOffSet) {
        Double width = getWidth();
        mLeft .draw(xOffset,                                  yOffSet + sLength);
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

