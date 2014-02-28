import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class HuffmanPanel extends JPanel
{
    private Huffman mHuffman;

    HuffmanPanel(Huffman hufmann) { mHuffman = hufmann; }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        setBackground(new Color(0.8F, 0.8F, 1.0F));
        super.paintComponent(g);
        double totalWidth  = 0.0;
        double totalHeight = 0.0;
        PriorityQueue<Node> queue     = mHuffman.getPriorityQueue();
        Node[]              nodeArray = queue.toArray(new Node[queue.size()]);
        Arrays.sort(nodeArray);
        for (Node node: nodeArray) {
            node.draw(totalWidth, 0.0);
            paintNode(g2, node);
            totalWidth += node.getWidth() + Node.sSeparation;
            totalHeight = Math.max(totalHeight, node.getHeight());
        }
        Dimension dimension = 
            new Dimension((int) totalWidth + 20, (int) totalHeight + 20);
        setPreferredSize(dimension);
    }
    // paint one tree
    private void paintNode(Graphics2D g2, Node node) {
        Point2D   top  = node.mTop;
        double    topX = top.getX();
        double    topY = top.getY();
        Ellipse2D circle = 
            new Ellipse2D.Double(topX - Node.sRadius, topY,
                                 Node.sDiameter, Node.sDiameter);
        g2.setPaint(Color.YELLOW);
        g2.fill(circle);
        g2.setPaint(Color.BLACK);
        if (node.isLeaf()) {                                  
            LeafNode  leaf = (LeafNode) node;
            g2.drawLine((int) (topX - Node.sRadius), (int) (topY + Node.sRadius),
                        (int) (topX + Node.sRadius), (int) (topY + Node.sRadius));
            g2.drawString(leaf.getCharacter() + "", 
                          (int) (topX - 4), (int) (topY + Node.sRadius + 20));
            g2.drawString(leaf.count() + "", (int) (topX - 4), (int) (topY + 20));
            return;
        }
        // it's a binary node
        g2.drawString(node.count() + "", 
                      (int) (topX - 0.3 * Node.sRadius), 
                      (int) (topY + 1.2 * Node.sRadius));
        paintNode(g2, node.getLeft());
        paintNode(g2, node.getRight());
        double  radius2   = Node.sRadius / Math.sqrt(2);
        Point2D topLeft   = node.getLeft ().mTop;
        Point2D topRight  = node.getRight().mTop;
        int     leftX     = (int) (topLeft. getX());
        int     rightX    = (int) (topRight.getX());
        int     leftY     = (int) (topLeft. getY());
        int     rightY    = (int) (topRight.getY());
        int     topXleft  = (int) (topX - radius2);
        int     topXright = (int) (topX + radius2);
        int     topYleft  = (int) (topY + Node.sRadius + radius2);
        int     topYright = (int) (topY + Node.sRadius + radius2);
        g2.drawLine(topXleft,  topYleft,  leftX,  leftY );
        g2.drawLine(topXright, topYright, rightX, rightY);
    }
}
/** This frame shows the array as it is sorted, together with buttons to single-step the animation
    or to run it without interruption.
*/
public class AnimationFrame extends JFrame
{
    private static final int DEFAULT_WIDTH  = 800;
    private static final int DEFAULT_HEIGHT = 800;

    Huffman    mHuffman;
    String []  mAlphabet;
    Integer[]  mFrequency;    
    JTextField mAlphabetField;
    JTextField mFrequencyField;

    public AnimationFrame() {
        mHuffman = new Huffman(new PriorityQueue<Node>());
        setTitle("Animation of the Huffman Code");
        JLabel            labelAlphabet     = new JLabel("Alphabet: ");
        JLabel            labelFrequency    = new JLabel("Frequencies: ");
        AlphabetListener  alphabetListener  = new AlphabetListener();
        FrequencyListener frequencyListener = new FrequencyListener();
        mAlphabetField  = new JTextField(16);
        mFrequencyField = new JTextField(16);
        mAlphabetField .setMaximumSize(mAlphabetField .getPreferredSize());
        mFrequencyField.setMaximumSize(mFrequencyField.getPreferredSize());
        mAlphabetField .addActionListener(alphabetListener );
        mFrequencyField.addActionListener(frequencyListener);
        JButton      stepButton   = new JButton("Step");
        StepListener stepListener = new StepListener();
        stepButton.addActionListener(stepListener);
        JPanel topPanel   = new JPanel();
        Box    firstPanel = Box.createHorizontalBox();
        topPanel  .setLayout(new BorderLayout());
        firstPanel.add(stepButton);
        firstPanel.add(Box.createHorizontalGlue());
        firstPanel.add(labelAlphabet);
        firstPanel.add(mAlphabetField);
        firstPanel.add(Box.createHorizontalGlue());
        firstPanel.add(labelFrequency);
        firstPanel.add(mFrequencyField);
        topPanel  .add(firstPanel, BorderLayout.NORTH);
        HuffmanPanel panel      = new HuffmanPanel(mHuffman);
        JScrollPane  scrollPane = new JScrollPane(panel);
        topPanel.add(scrollPane);
        add(topPanel);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }   

    public static void main(String[] args) {
        new AnimationFrame();
    }
    // Listener for the alphabet text field.
    class AlphabetListener implements ActionListener
    {        
        // Parse the characters and store them.
        public void actionPerformed(ActionEvent e) {
            String alphabet = mAlphabetField.getText().trim();
            mAlphabet = alphabet.split("[, ]+");
            System.out.println("mAlphabet initialized");
            for (int i = 0; i < mAlphabet.length; ++i) {
                System.out.println(mAlphabet[i]);
            }
        }
    }
    // Listener for the frequency text field.
    class FrequencyListener implements ActionListener
    {        
        // Parse the frequencies and store them.
        public void actionPerformed(ActionEvent e) {
            String   frequencyString = mFrequencyField.getText().trim();
            String[] frequencyArray  = frequencyString.split("[, ]+");
            mFrequency = new Integer[frequencyArray.length];
            for (int i = 0; i < mFrequency.length; ++i) {
                mFrequency[i] = Integer.parseInt(frequencyArray[i]);
            }
            System.out.println("mFrequency initialized");
            for (int i = 0; i < mFrequency.length; ++i) {
                System.out.println(mFrequency[i]);
            }
        }
    }
    class StepListener implements ActionListener
    {    
        public void actionPerformed(ActionEvent e) {
            PriorityQueue<Node> queue = mHuffman.getPriorityQueue();
            if (queue.size() == 0 && mAlphabet.length > 1 && mFrequency.length > 1) {
                int m = Math.min(mAlphabet.length, mFrequency.length);
                for (int i = 0; i < m; ++i) {
                    queue.add(new LeafNode(mAlphabet[i].charAt(0), mFrequency[i]));
                }
                repaint();
            } else {
                mHuffman.oneStep();
                repaint();
            }
        }
    }
}


