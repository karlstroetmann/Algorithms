import java.util.*;
import java.awt.geom.*;

public class LeafNode extends Node {
	private char mCharacter;
	private int  mFrequency;
	
	public LeafNode(char character, int frequency) {
		mCharacter = character;
		mFrequency = frequency;
	}
	public Integer cost() {
		return 0;
	}
	public Integer count() {
		return mFrequency;
	}
	public Character getCharacter() {
		return mCharacter;
	}
	public String toString() {
		return "leaf(" + mCharacter + ", " + mFrequency + ")";
	}
	// animation
    public void draw(double xOffset, double yOffSet) {
        mTop = new Point2D.Double(xOffset + sRadius, yOffSet);
    }
    public double  getWidth () { return sDiameter; }
    public double  getHeight() { return sDiameter; }
    public Node    getLeft  () { return null;      }
    public Node    getRight () { return null;      }
    public boolean isLeaf   () { return true;      }
}