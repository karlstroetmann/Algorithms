import java.awt.geom.*;

public abstract class Node implements Comparable<Node> {
	public    abstract Integer cost();
	public    abstract Integer count();
	public    abstract String  toString();
	public int compareTo(Node rhs) {
		return count().compareTo(rhs.count());
	}
    // methods below are only necessary for the animation
    Point2D mTop;          // position of the topmost pixel in the 
                           // circle representing this node
    static final double sLength        = 70.0;  // vertical separation of nodes
    static final double sRadius        = 25.0;  // radius of circle representing node
    static final double sDiameter      = 2.0 * sRadius;
    static final double sSeparation    = 20.0;  // horizontal separation between trees

    public abstract void    draw(double xOffset, double yOffSet);
    public abstract double  getWidth ();
    public abstract double  getHeight();
    public abstract Node    getLeft  ();
    public abstract Node    getRight ();
    public abstract boolean isLeaf   ();
}