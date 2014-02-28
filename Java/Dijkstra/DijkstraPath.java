import java.util.*;
import java.io.*;

public class DijkstraPath
{
    Map<String, Node> mNodeMap;
    
    public DijkstraPath(String fileName) {
        try {
            FileReader     fileReader     = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String         line           = bufferedReader.readLine();
            String[]       nodes          = line.split("[, ]+");
            mNodeMap = new TreeMap<String, Node>();
            for (String name: nodes) {
                Node node = new Node(name);
                mNodeMap.put(name, node);
            }
            while ((line = bufferedReader.readLine()) != null) {
                String[]   edgeArray  = line.split("[, ]+");
                String     sourceName = edgeArray[0];
                String     targetName = edgeArray[1];
                Integer    length     = Integer.parseInt(edgeArray[2]);
                Node       source     = mNodeMap.get(sourceName);
                Node       target     = mNodeMap.get(targetName);
                Edge       edge       = new Edge(source, target, length);
                List<Edge> edges      = source.getEdges();
                edges.add(edge);
                source.setEdges(edges);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Node> shortestPath(Node source, Node goal) {
        Map<Node, Integer>      dist     = new TreeMap<Node, Integer>();
        HeapTree<Integer, Node> fringe   = new HeapTree<Integer, Node>();
        Map<Node, Node>         backEdge = new TreeMap<Node, Node>();
        dist.put(source, 0);
        fringe.insert(0, source);
        while (!fringe.isEmpty()) {
            Pair<Integer, Node> p     = fringe.top();
            Integer             distU = p.getFirst();
            Node                u     = p.getSecond();
            if (u.equals(goal)) {
                return constructPath(source, goal, backEdge);
            }
            fringe.remove();
            for (Edge edge: u.getEdges()) {
                Node v = edge.getTarget();
                if (dist.get(v) == null) {
                    Integer d = distU + edge.getLength();
                    dist.put(v, d);
                    fringe.insert(d, v);
                    backEdge.put(v, u);
                } else {
                    Integer oldDist = dist.get(v);
                    Integer newDist = dist.get(u) + edge.getLength();
                    if (newDist < oldDist) {
                        dist.put(v, newDist);
                        fringe.change(newDist, v);
                    }
                    backEdge.put(v, u);
                }
            }
        }
        return null;
    }
    List<Node> constructPath(Node source, Node goal, Map<Node, Node> backEdge) {
        if (source.equals(goal)) {
            List<Node> result = new LinkedList<Node>();
            result.add(source);
            return result;
        }
        Node n = backEdge.get(goal);
        List<Node> result = constructPath(source, n, backEdge);
        result.add(goal);
        return result;
    }
    public static void main(String[] args) {
        DijkstraPath graph  = new DijkstraPath(args[0]);
        Node         source = graph.mNodeMap.get(args[1]);
        Node         goal   = graph.mNodeMap.get(args[2]);
        List<Node>   path = graph.shortestPath(source, goal);
        for (Node node: path) {
            System.out.print(node + " ");
        }
        System.out.println("");
    }
}



