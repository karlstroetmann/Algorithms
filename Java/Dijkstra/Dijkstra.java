import java.util.*;
import java.io.*;

public class Dijkstra
{
    Map<String, Node> mNodeMap;
    
     public Dijkstra(String fileName) {
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
    public Map<Node, Integer> shortestPath(Node source) 
    {
        Map<Node, Integer> dist = new TreeMap<Node, Integer>();
        dist.put(source, 0);
        HeapTree<Integer, Node> fringe = new HeapTree<Integer, Node>();
        fringe.insert(0, source);
        while (!fringe.isEmpty()) {
            Pair<Integer, Node> p     = fringe.top();
            Integer             distU = p.getFirst();
            Node                u     = p.getSecond();
            fringe.remove();
            for (Edge edge: u.getEdges()) {
                Node v = edge.getTarget();
                if (dist.get(v) == null) {
                    Integer d = distU + edge.getLength();
                    dist.put(v, d);
                    fringe.insert(d, v);
                } else {
                    Integer oldDist = dist.get(v);
                    Integer newDist = dist.get(u) + edge.getLength();
                    if (newDist < oldDist) {
                        dist.put(v, newDist);
                        fringe.change(newDist, v);
                    }
                }
            }
        }
        return dist;
    }
    public static void main(String[] args) {
        Dijkstra graph  = new Dijkstra(args[0]);
        Node     source = graph.mNodeMap.get(args[1]);
        Map<Node, Integer> dist = graph.shortestPath(source);
        for (Node node: dist.keySet()) {
            System.out.println(node + ": " + dist.get(node));
        }
    }
}



