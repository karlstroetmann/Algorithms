import java.awt.geom.*;

/** This class represents an empty node. */
public class EmptyNode<Key extends Comparable<Key>, Value> 
    extends Node<Key, Value>
{
	public EmptyNode() {
		mHeight = 0;
	}
	
	// Find the value attached to the key given as argument.
	public Value find(Key key) {
		return null;
	}
	
	// Insert the given <key, value> pair into the tree.
	public Node<Key, Value> insert(Key key, Value value) {
		return new BinaryNode<Key, Value>(key, value);
	}
	
	// Delete the given key from the tree.
	public Node<Key, Value> delete(Key key) {
		return this;
	}

	public boolean isEmpty() {
		return true;
	}

	Triple<Node<Key, Value>, Key, Value> delMin() {
		throw new UnsupportedOperationException();
	}

	void restore() { return; }

    public String toString() {
        return "nil";
    }

    public Key getMaxKey() {
		throw new UnsupportedOperationException();
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
