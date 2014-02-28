import java.util.*;

public class TrieNode<Value> implements MyMap<String, Value>
{
    Value               mValue;    // value for empty string
    List<Character>       mCharList; // list of characters
    List<TrieNode<Value>> mNodeList; // list of tries

    // construct a trie representing the empty map {}
    TrieNode() {
        mValue    = null;
        mCharList = new ArrayList<Character>(0);
        mNodeList = new ArrayList<TrieNode<Value>>(0);
    }

    // Find the given key.
    public Value find(String key) {
        if (key.length() == 0) {
            return mValue;
        } else {
            Character firstChar = key.charAt(0);
            String    rest      = key.substring(1);
            for (int i = 0; i < mCharList.size(); ++i) {
                if (firstChar.equals(mCharList.get(i))) {
                    return mNodeList.get(i).find(rest);
                }
            }
            return null;
        }
    }

    // Insert the given <key, value> pair into the trie.
    public void insert(String key, Value value) {
        if (key.length() == 0) {
            mValue = value;
        } else {
            Character firstChar = key.charAt(0);
            String    rest      = key.substring(1);
            for (int i = 0; i < mCharList.size(); ++i) {
                if (firstChar.equals(mCharList.get(i))) {
                    mNodeList.get(i).insert(rest, value);
                    return;
                }
            }
            mCharList.add(firstChar);
            TrieNode<Value> node = new TrieNode<Value>();
            node.insert(rest, value);
            mNodeList.add(node);
        }
    }

    // Remove the given key from the trie.
    public void delete(String key) {
        if (key.length() == 0) {
            mValue = null;
            return;
        } 
        Character firstChar = key.charAt(0);
        String    rest      = key.substring(1);
        for (int i = 0; i < mCharList.size(); ++i) {
            if (firstChar.equals(mCharList.get(i))) {
                TrieNode<Value> node = mNodeList.get(i);
                node.delete(rest);
                if (node.isEmpty()) {
                    mCharList.remove(i);
                    mNodeList.remove(i);
                } 
				return;
            }
        }
    }

    // Return true if this node represents the empty map.
    public Boolean isEmpty() {
		if (mValue != null) {
			return false;
		}
		for (TrieNode<Value> node : mNodeList) {
			if (!node.isEmpty()) {
				return false;
			}
		}
        return true;
    }

    public String toString() {
        String result = "";
        if (mValue != null) {
            result += "\"\" |-> " + mValue;
        }
        result += "[";
        for (int i = 0; i < mCharList.size(); ++i) {
            result += mCharList.get(i) + " |-> " + mNodeList.get(i);
            if (i + 1 < mCharList.size()) {
                result += ", ";
            }
        }
        result += "]";
        return result;
    }
}
