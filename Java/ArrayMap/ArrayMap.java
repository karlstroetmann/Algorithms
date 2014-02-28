public class ArrayMap<Value> implements MyMap<Integer, Value>
{
    Value[] mArray;
    
    public ArrayMap(int n) {
        mArray = (Value[]) new Object[n+1];
    }
    public Value find(Integer key) {
        return mArray[key];
    }
    public void insert(Integer key, Value value) {
        mArray[key] = value;
    }
    public void delete(Integer key) {
        mArray[key] = null;
    }
}
