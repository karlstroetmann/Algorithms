import java.util.*;

public class Node implements Comparable<Node>
{
    private String     mName;
    private List<Edge> mEdges;

    public Node(String name) {
        mName  = name;
        mEdges = new LinkedList<Edge>();
    }    
    public String     toString() { return mName;  }
    public String     getName () { return mName;  }
    public List<Edge> getEdges() { return mEdges; }
    public void setEdges(List<Edge> edges) { mEdges = edges; }
        
    public int compareTo(Node node) {
        return mName.compareTo(node.mName);
    }
    public boolean equals(Node node) {
        return mName.equals(node.mName);
    }
}
