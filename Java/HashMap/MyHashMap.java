import java.util.*;

public class MyHashMap<Key, Value> implements MyMap<Key, Value>
{
    // the load factor determines the average length of the lists
    static final double sAlpha = 2;

    // This is an array of prime numbers that roughly double in size
    static final int[] sPrimes = {              3,
                                                7,
                                               13,
                                               31,
                                               61,
                                              127,
                                              251,
                                              509,
                                             1021,
                                             2039,
                                             4093,
                                             8191,
                                            16381,
                                            32749,
                                            65521,
                                           131071,
                                           262139,
                                           524287,
                                          1048573,
                                          2097143,
                                          4194301,
                                          8388593,
                                         16777213,
                                         33554393,
                                         67108859,
                                        134217689,
                                        268435399,
                                        536870909,
                                       1073741789,
                                       2147483647 };

    // this is the hash array used to store all Values
    Object[] mArray;
    // The size of mArray is given by sPrimes[mPrimeIndex]
    int mPrimeIndex;

    // This variables counts the number of entries in this hash table.
    int mNumberEntries;

    public MyHashMap(int primeIndex) {
        mPrimeIndex = primeIndex;
        int size    = sPrimes[mPrimeIndex];
        mArray      = new Object[size];
    }

    // Look up a given key.
    public Value find(Key key) {
        int index = Math.abs(key.hashCode() % mArray.length);
        LinkedList<Pair<Key, Value>> list  = 
            (LinkedList<Pair<Key, Value>>) mArray[index];
        if (list == null) {
            return null;
        }
        for (int i = 0; i < list.size(); ++i) {
            Pair<Key, Value> pair = list.get(i);
            if (key.equals(pair.getFirst())) {
                return pair.getSecond();
            }
        }
        return null;
    }
    
    // Insert a new <key, value> pair into the hash map.
    public void insert(Key key, Value value) {
        if (mNumberEntries / (double) mArray.length > sAlpha) {
            rehash();
        }
        int index = Math.abs(key.hashCode() % mArray.length);
        LinkedList<Pair<Key, Value>> list  = 
            (LinkedList<Pair<Key, Value>>) mArray[index];
        if (list == null) {
            list          = new LinkedList<Pair<Key, Value>>();
            mArray[index] = list;
        }
        for (int i = 0; i < list.size(); ++i) {
            Pair<Key, Value> pair = list.get(i);
            if (key.equals(pair.getFirst())) {
                pair.setSecond(value);
                return;
            }
        }
        list.add(new Pair<Key, Value>(key, value));
        ++mNumberEntries;
    }

    // Increase the size of the hash table.
    private void rehash() {
        ++mPrimeIndex;
        MyHashMap<Key, Value> bigMap = new MyHashMap<Key, Value>(mPrimeIndex);
        for (Object list: mArray) {
            if (list == null) {
                continue;
            }
            for (Object object: (LinkedList<Pair<Key, Value>>) list) {
                Pair<Key, Value> pair = (Pair<Key, Value>) object;
                bigMap.insert(pair.getFirst(), pair.getSecond());
            }
        }
        mArray = bigMap.mArray;
    }
    
    // Delete a pair containing the given key.
    public void delete(Key key) {
        int index = Math.abs(key.hashCode() % mArray.length);
        LinkedList<Pair<Key, Value>> list  = 
            (LinkedList<Pair<Key, Value>>) mArray[index];
        if (list == null) {
            return;
        }
        for (int i = 0; i < list.size(); ++i) {
            Pair<Key, Value> pair = list.get(i);
            if (key.equals(pair.getFirst())) {
                list.remove(i);
                --mNumberEntries;
                return;
            }
        }
    }
}

        