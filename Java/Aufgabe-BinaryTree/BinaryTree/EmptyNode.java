/** This class represents an empty node. */
public class EmptyNode<Key extends Comparable, Value> 
	extends Node<Key, Value>
{
	public EmptyNode() {}
	
	// Find the value attached to the key given as argument.
	public Value find(Key key) {
		return null;
	}
	
	// Insert the given <key, value> pair into the tree.
	public Node<Key, Value> insert(Key key, Value value) {
		return new BinaryNode(key, value);
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
}
