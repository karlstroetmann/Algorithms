import java.util.*;
import java.io.*;

public class Huffman {
    Map<Character, Integer> mFrequencyTable;
    Node                    mCoding;      // the coding tree 

    // Read the given file, transform its contents into a string, 
    // and compute the Hufman coding for this string.
    public Huffman(String fileName) {
        determineFrequencies(fileName);
        mCoding = createHuffmanCode();
    }

    // read the given file, compute the frequency of each character,
    // store these frequencies in the the frequency table and initialize
    // the arrays mCharArray and mFrequencies.
    public void determineFrequencies(String fileName) {
        mFrequencyTable = new TreeMap<Character, Integer>();
        for (char c = 1; c < 128; ++c) {
            mFrequencyTable.put(c, 0);
        }
        int numberChars = 0;
        try {
            FileReader fr = new FileReader(fileName);
            while (true) {
                char c = (char) fr.read();
                if (c == 65535) { // EOF
                    break;
                }
                ++numberChars;
                int count = mFrequencyTable.get(c);
                ++count;
                mFrequencyTable.put(c, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Number of characters read: " + numberChars);
    }

    public Node createHuffmanCode() {
        PriorityQueue<Node> queue = new PriorityQueue<Node>();
        for (Character c: mFrequencyTable.keySet()) {
            Integer frequency = mFrequencyTable.get(c);
            if (frequency >= 1) {
                LeafNode leaf = new LeafNode(c, frequency);
                queue.offer(leaf);
            }
        }
        while (queue.size() > 1) {
            Node left  = queue.remove();
            Node right = queue.remove();
            Node node  = new BinaryNode(left, right);
            queue.offer(node);
        }
        return queue.peek();
    }

    public static void main(String[] args) {
        Huffman hc = new Huffman(args[0]);
        Node    n  = hc.mCoding;
        System.out.println(n.toString());
        System.out.printf("cost: %d Bits = %d Bytes", n.cost(), n.cost() / 8);
    }
}