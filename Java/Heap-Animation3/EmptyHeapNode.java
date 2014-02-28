import java.util.*;
import java.awt.geom.*;

/** This class represents an empty node. */
public class EmptyHeapNode<Key extends Comparable<Key>, Value> 
    extends HeapNode<Key, Value>
{
    public EmptyHeapNode(BinaryHeapNode<Key, Value>             parent,
                         Map<Value, BinaryHeapNode<Key, Value>> nodeMap) 
    {
        mParent  = parent;
        mNodeMap = nodeMap;
        mCount   = 0;
    }

    public Pair<Key, Value> top() {
        return null;
    }
    
    public BinaryHeapNode<Key, Value> insert(Key key, Value value) {
        BinaryHeapNode<Key, Value> binaryNode =
            new BinaryHeapNode<Key, Value>(key, value, mParent, mNodeMap);
        mNodeMap.put(value, binaryNode);
        return binaryNode;
    }
    
    public HeapNode<Key, Value> remove() {
        return this;
    }

    public void change(Key key, Value value) {}

    public boolean isEmpty() {
        return true;
    }

    public String toString() {
        return "nil";
    }

    // All methods below this line are for the graphical animation

    public void draw(double xOffset, double yOffSet) {
        mTop = new Point2D.Double(xOffset + sSmallRadius, yOffSet);
    }
    
    public double getWidth() {
        return sSmallDiameter;
    }
    
    public double getHeight() {
        return sSmallDiameter;
    }    
}
