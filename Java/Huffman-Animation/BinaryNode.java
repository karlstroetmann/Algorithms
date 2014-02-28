import java.awt.geom.*;

// represents node(mLeft, mRight)
public class BinaryNode extends Node {
	private Node    mLeft;
	private Node    mRight;
	private Integer mCount;
	private Integer mCost;

	public BinaryNode(Node left, Node right) {
		mLeft  = left;
		mRight = right;
		mCount = mLeft.count() + mRight.count();
		mCost  = mLeft.cost()  + mRight.cost() + mCount;
	}
	public Integer cost () { return mCost;  }
	public Integer count() { return mCount; }
	public String toString() {
		String result = "node(";
		result += mLeft;
		result += ", ";
		result += mRight;
		result += "):";
		result += mCount;
		return result;
	}
	// animation
    public void draw(double xOffset, double yOffSet) {
        Double width = getWidth();
        mLeft .draw(xOffset,                                  yOffSet + sLength);
        mRight.draw(xOffset + mLeft.getWidth() + sSeparation, yOffSet + sLength);
        mTop = new Point2D.Double(xOffset + 0.5 * width, yOffSet);
    }
    public double getWidth() {
        return mLeft.getWidth() + mRight.getWidth() + sSeparation;
    }
    public double getHeight() {
        return Math.max(mLeft.getHeight(), mRight.getHeight()) + sLength;
    }
	public Node    getLeft () { return mLeft ; }
    public Node    getRight() { return mRight; }
    public boolean isLeaf  () { return false ; }
}