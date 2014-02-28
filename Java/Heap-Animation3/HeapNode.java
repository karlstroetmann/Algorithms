import java.util.*;
import java.awt.geom.*;

/** This class represents nodes of a binary tree representing a heap. */
public abstract class HeapNode<Key extends Comparable<Key>, Value>
{
    protected int                        mCount;  // the number of nodes
    protected BinaryHeapNode<Key, Value> mParent; // parent of this node
    // This map stores the nodes associated with the values.
    protected Map<Value, BinaryHeapNode<Key, Value>> mNodeMap;
    Point2D mTop;          // position of the topmost pixel in the 
                           // circle representing this node
    static final double sLength        = 70.0;
    static final double sRadius        = 25.0;
    static final double sDiameter      = 2.0 * sRadius;
    static final double sSmallRadius   = 0.5 * sRadius;
    static final double sSmallDiameter = 2.0 * sSmallRadius;
    static final double sSeparation    = 20.0;

    public abstract Pair<Key, Value> top();
    public abstract BinaryHeapNode<Key, Value> insert(Key key, Value value);    
    public abstract HeapNode<Key, Value> remove();
    public abstract void change(Key k, Value v);
    // Tell whether this is the empty node.
    public abstract boolean isEmpty();
    public abstract String  toString();
    // methods below are only necessary for the animation
    public abstract void draw(double xOffset, double yOffSet);
    public abstract double getWidth ();
    public abstract double getHeight();
    public HeapNode<Key, Value> getLeft()  { return null; }
    public HeapNode<Key, Value> getRight() { return null; }
    public Key                  getKey()   { return null; }
    public Value                getValue() { return null; }
}

        

    