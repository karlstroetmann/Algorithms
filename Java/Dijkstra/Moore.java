import java.util.*;

public class Moore 
{
	Map<Node, Set<Edge>> mEdges;
	Map<Edge, Integer>   mLenght;
	
    public Map<Node, Integer> shortestPath(Node source) 
	{
		Map<Node, Integer> distSource = new TreeMap<Node, Integer>();
		distSource.put(source, 0);
		LinkedList<Node> fringe = new LinkedList<Node>();
		while (!fringe.isEmpty()) {
			Node u = fringe.get(0);
			fringe.remove(0);
			for (Edge edge: mEdges.get(u)) {
				Node v = edge.getTarget();
				if (distSource.get(v) == null) {
					distSource.put(v, distSource.get(u) + mLenght.get(edge));
					fringe.add(v);
				} else {
					Integer oldDist = distSource.get(v);
					Integer newDist = distSource.get(u) + mLenght.get(edge);
					if (newDist < oldDist) {
						distSource.put(v, newDist);
						fringe.add(v);
					}
				}
			}
		}
		return distSource;
	}
}

