import java.util.*;

/** This class represents an empty node. */
public class EmptyNode<Key extends Comparable<Key>, Value> 
	extends Node<Key, Value>
{
	public EmptyNode() {}
	
	public Value find(Key key) {
		return null;
	}
	
	public Node<Key, Value> insert(Key key, Value value) {
		return new BinaryNode<Key, Value>(key, value);
	}
	
	public Node<Key, Value> delete(Key key) {
		return this;
	}

	public boolean isEmpty() {
		return true;
	}

	Triple<Node<Key, Value>, Key, Value> delMin() {
		throw new UnsupportedOperationException();
	}

	public LinkedList<Key> toList() {
		return new LinkedList<Key>();
	}
}
