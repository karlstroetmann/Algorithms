public interface MyMap<Key extends Comparable, Value>
{
	public Value find(Key key);
	public void  insert(Key key, Value value);
	public void delete(Key key);
}
	