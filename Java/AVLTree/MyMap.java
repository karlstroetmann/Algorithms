/** \interface MyMap
 *  This interface describes the mathematical concept of a map.
 */
public interface MyMap<Key extends Comparable<Key>, Value>
{
	/** Returns the value stored under the given key.
	 *  @param  key The key to be searched.
	 *  @return     The value stored under the given key.
	 */
	public Value find(Key key);
	/** Inserts the given value under the given key.
	 *  @param  key   The key under which the value has to be entered.
	 *  @param  value The value to be entered.
	 */
	public void  insert(Key key, Value value);
	/** Deletes the given key from the map.
	 *  @param  key The key to be deleted.
	 */
	public void  delete(Key key);
}
	