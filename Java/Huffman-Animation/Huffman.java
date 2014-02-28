import java.util.*;

public class Huffman {
	PriorityQueue<Node> mPriorityQueue;

	public Huffman(PriorityQueue<Node> priorityQueue) {
		mPriorityQueue = priorityQueue;
	}
	public void oneStep() {
		System.out.println(this);
		if (mPriorityQueue.size() > 1) {
			Node left  = mPriorityQueue.remove();
			Node right = mPriorityQueue.remove();
			Node node  = new BinaryNode(left, right);
			mPriorityQueue.add(node);
			System.out.println(this);
		}
	}
	public String toString() {
		String result = "";
		Node[] nodeArray = mPriorityQueue.toArray(new Node[mPriorityQueue.size()]);
		Arrays.sort(nodeArray);
		for (int i = 0; i < nodeArray.length; ++i) {
			result += nodeArray[i];
			if (i + 1 < nodeArray.length) {
				result += ", ";
			}
		}
		return result;
	}
	PriorityQueue<Node> getPriorityQueue() {
		return mPriorityQueue;
	}
}