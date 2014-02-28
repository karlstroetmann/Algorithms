import java.util.*;

// represents node(mLeft, mRight)
public class BinaryNode extends Node {
    private Node mLeft;
    private Node mRight;
    private int  mCount;
    private int  mCost;

    public BinaryNode(Node left, Node right) {
        mLeft  = left;
        mRight = right;
        mCount = mLeft.count() + mRight.count();
        mCost  = mLeft.cost()  + mRight.cost() + mCount;
    }
    public Integer cost() {
        return mCost;
    }
    public Integer count() {
        return mCount;
    }
    public String toString() {
        String result = "[\n";
        ArrayList<String> list = toStringArray();
        for (int i = 0; i < list.size(); ++i) {
            result += list.get(i);
            if (i + 1 < list.size()) {
                result += "\n";
            }
        }
        result += "\n]\n";
        return result;
    }
    protected ArrayList<String> toStringArray() {
        ArrayList<String> result    = new ArrayList<String>();
        ArrayList<String> leftList  = mLeft .toStringArray();
        ArrayList<String> rightList = mRight.toStringArray();
        for (String code : leftList) {
            result.add("0" + code);
        }
        for (String code : rightList) {
            result.add("1" + code);
        }
        return result;
    }
}