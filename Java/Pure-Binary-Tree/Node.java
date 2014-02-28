import java.util.*;

/** This class represents nodes of a binary tree. */
public abstract class Node<Key extends Comparable<Key>, Value>
{
	// Find the value attached to the key given as argument.
	public abstract Value find(Key key);
	
	// Insert the given <key, value> pair into the tree.
	public abstract Node<Key, Value> insert(Key key, Value value);
	
	// Delete the given key from the tree.
	public abstract Node<Key, Value> delete(Key key);

	// Tell whether this is the empty node.
	public abstract boolean isEmpty();

	abstract Triple<Node<Key, Value>, Key, Value> delMin();

	// Transform the tree into a sorted list.
	public abstract LinkedList<Key> toList();
}

		

	