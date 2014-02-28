import java.awt.geom.*;

/** This class represents nodes of a binary tree. */
public abstract class Node<Key extends Comparable<Key>, Value>
{
	protected int mHeight;  // the height of the tree

	Point2D mTop;	

	static final double sLength        = 50.0;
	static final double sRadius        = 15.0;
	static final double sDiameter      = 2.0 * sRadius;
	static final double sSmallRadius   = 0.5 * sRadius;
	static final double sSmallDiameter = 2.0 * sSmallRadius;
	static final double sSeparation    = 15.0;

	// Find the value attached to the key given as argument.
	public abstract Value find(Key key);
	
	// Insert the given <key, value> pair into the tree.
	public abstract Node<Key, Value> insert(Key key, Value value);
	
	// Delete the given key from the tree.
	public abstract Node<Key, Value> delete(Key key);

	// Tell whether this is the empty node.
	public abstract boolean isEmpty();

	abstract Triple<Node<Key, Value>, Key, Value> delMin();

	abstract void restore();

	public abstract String toString();

    // Generate a lopsided AVL tree with height h, where k is the smallest Key.
    public static Node<Integer, Integer> genSkewedTree(int h, int k) {
		if (h == 0) {
            return new EmptyNode<Integer, Integer>();
        }
        if (h == 1) {
            Node<Integer, Integer> l = new EmptyNode<Integer, Integer>();
            Node<Integer, Integer> r = new EmptyNode<Integer, Integer>();
            return new BinaryNode<Integer, Integer>(k, 0, l, r);
        }
        Node<Integer, Integer> left  = genSkewedTree(h - 1, k);
        Integer                kMax  = left.getMaxKey();
        Node<Integer, Integer> right = genSkewedTree(h - 2, kMax + 2);
        return new BinaryNode<Integer, Integer>(kMax + 1, 0, left, right);
    }

	// get the largest key in the tree
    public abstract Key getMaxKey();

	// methods below are only necessary for the animation
	public abstract void draw(double xOffset, double yOffSet);

	public abstract double getWidth();
	
	public abstract double getHeight();

	public Node<Key, Value> getLeft()  { return null; }
	public Node<Key, Value> getRight() { return null; }

	public Key   getKey()   { return null; }
	public Value getValue() { return null; }
}

		

	