import java.util.*;

public abstract class Node implements Comparable<Node> {
    public    abstract Integer cost();
    public    abstract Integer count();
    public    abstract String  toString();
    protected abstract ArrayList<String> toStringArray();

    public int compareTo(Node rhs) {
        return count().compareTo(rhs.count());
    }
}