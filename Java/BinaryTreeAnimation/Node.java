import java.awt.geom.*;

/** This class represents nodes of a binary tree. */
public abstract class Node<Key extends Comparable<Key>, Value>
{
	Point2D mTop;	

	static final double sLength        = 50.0;  // height of box containing one node
	static final double sRadius        = 15.0;  // radius of circle representing node
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

	public abstract String toString();

	public abstract void draw(double xOffset, double yOffSet);

	public abstract double getWidth();
	
	public abstract double getHeight();

	public Node getLeft()  { return null; }
	public Node getRight() { return null; }

	public Key   getKey()   { return null; }
	public Value getValue() { return null; }

}

		

	