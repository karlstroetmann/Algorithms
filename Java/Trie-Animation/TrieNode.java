import java.util.*;
import java.awt.geom.*;

import static java.lang.Math.*;

public class TrieNode<Value> implements MyMap<String, Value>
{
	// The list of characters for which this node stores tries.
	ArrayList<Character> mCharList;
    // The tries associated with the characters.
	ArrayList<TrieNode<Value>> mNodeList;
	// the value associated with the empty key
	Value                  mValue;

	// construct a trie representing the empty map {}
	TrieNode() {
		mCharList = new ArrayList<Character>(0);
		mNodeList = new ArrayList<TrieNode<Value>>(0);
		mValue    = null;
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

	// All code below this line is used for the graphical animation.
	
	Point2D mTop;  // The coordinates of the topmost point of the circle
	               // representing this node

	static final double sLength        = 70.0;  // height of box containing one node
	static final double sRadius        = 25.0;  // radius of circle representing node
	static final double sDiameter      = 2.0 * sRadius;
	static final double sSeparation    = 15.0;

	// Computes the point mTop.  mTop is set at the middle of the upper
	// edge of a box.  The top left corner of this box has the coordinates
	// (xOffset, yOffSet).
    public void computeTop(double xOffset, double yOffSet) {
		double offset = xOffset;
		for (int i = 0; i < mNodeList.size(); ++i) {
			mNodeList.get(i).computeTop(offset, yOffSet + sLength);
			offset += mNodeList.get(i).getWidth();
			if (i < mNodeList.size() - 1) {
				offset += sSeparation;
			}
		}
        mTop = new Point2D.Double(xOffset + 0.5 * getWidth(), yOffSet);
    }

	// Compute the width of a box containing this node.
    public double getWidth() {
		if (mCharList.size() == 0) {
			return sDiameter;
		}		
		double result = 0.0;
		for (int i = 0; i < mCharList.size(); ++i) {
			result += mNodeList.get(i).getWidth();
			if (i < mCharList.size() - 1) {
				result += sSeparation;
			}
		}
        return result;
    }
    
	// Compute the height of a box containing this node.
    public double getHeight() {
		if (mCharList.size() == 0) {
			return sDiameter;
		}		
		double maxHeight = 0;
		for (TrieNode<Value> node: mNodeList) {
			maxHeight = max(maxHeight, node.getHeight());
		}
		return maxHeight + sLength;
    }

}
