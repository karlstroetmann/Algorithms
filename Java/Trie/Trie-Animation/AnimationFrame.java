import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import javax.swing.event.*;

import static java.lang.Math.*;

class TreePanel extends JPanel
{
    private TrieNode<String> mTrie = null;

    TreePanel(TrieNode<String> trie) {
        mTrie = trie;
    }

    // Paint the trie starting from the root node.
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        setBackground(new Color(0.8F, 0.8F, 1.0F));
        super.paintComponent(g);
        //        TrieNode<String> root = mTrie.mRoot;
        mTrie.computeTop(5.0, 5.0);
        paintNode(g2, mTrie);
        Dimension dimension = 
            new Dimension((int) mTrie.getWidth() + 10, (int) mTrie.getHeight() + 10);
        setPreferredSize(dimension);
    }

    // Paint the given node and all its descendents.
    private void paintNode(Graphics2D g2, TrieNode<String> node) {
        double    topX   = node.mTop.getX();
        double    topY   = node.mTop.getY();
        double    width  = node.getWidth();
        Ellipse2D circle = new Ellipse2D.Double(topX - TrieNode.sRadius, topY,
                                                TrieNode.sDiameter, TrieNode.sDiameter);
        g2.setPaint(Color.YELLOW);
        g2.fill(circle);
        g2.setPaint(Color.BLACK);
        // draw line dividing circle in half
        g2.drawLine((int) (topX - TrieNode.sRadius), (int) (topY + TrieNode.sRadius),
                    (int) (topX + TrieNode.sRadius), (int) (topY + TrieNode.sRadius));
        if (node.mValue != null) {
            g2.setPaint(Color.BLUE);
            g2.drawString(node.mValue.toString(), 
                          (int) (topX - 0.5 * TrieNode.sRadius), 
                          (int) (topY + 1.6 * TrieNode.sRadius));
        }
        for (int i = 0; i < node.mNodeList.size(); ++i) {
            TrieNode<String> u = node.mNodeList.get(i);
            Point2D p = u.mTop;
            double  x = p.getX();
            double  y = p.getY();
            double phi  = atan( (x - topX) / (y - topY) );
            // (outX, outY) is the point on the perimeter of the circle where the line
            // connecting the child starts.
            int outX = (int) (topX + TrieNode.sRadius * sin(phi));
            int outY = (int) (topY + TrieNode.sRadius * (1.0 + cos(phi)));
            // draw line connecting parent and child
            g2.drawLine(outX, outY, (int) x, (int) y);
            paintNode(g2, u);
            g2.setPaint(Color.BLUE);
            g2.drawString(node.mCharList.get(i).toString(), 
                          (int) (x - 0.15 * TrieNode.sRadius),
                          (int) (y + 0.8 * TrieNode.sRadius));
            g2.setPaint(Color.BLACK);
        }        
    }
}


/** This frame shows the array as it is sorted, together with buttons to single-step the animation
    or to run it without interruption.
*/
public class AnimationFrame extends JFrame
{
    private static final int DEFAULT_WIDTH  = 800;
    private static final int DEFAULT_HEIGHT = 800;

    TrieNode<String> mTrie;
    JTextField   mInsertField;
    JTextField   mDeleteField;

    /** Get the number of values to be sorted. */
    public AnimationFrame() {
        mTrie = new TrieNode<String>();

	mTrie.insert("georg",     "000");
	mTrie.insert("jenny",     "001");
	mTrie.insert("sebastian", "002");
	mTrie.insert("marc",      "003");
	mTrie.insert("christian", "004");
	mTrie.insert("steffen",   "005");
	mTrie.insert("carsten",   "006");
	mTrie.insert("richard",   "007");
	mTrie.insert("susann",    "008");
	mTrie.insert("miriam",    "009");
	mTrie.insert("jonas",     "010");
	mTrie.insert("hannes",    "011");
	mTrie.insert("michael",   "012");
	mTrie.insert("stephan",   "013");
	mTrie.insert("benjamin",  "014");
	mTrie.insert("tino",      "015");
	mTrie.insert("arndt",     "016");
	mTrie.insert("ansgar",    "017");
	mTrie.insert("daniel",    "018");
	mTrie.insert("stefan",    "019");
	mTrie.insert("anjo",      "020");
	mTrie.insert("marcel",    "021");    

        JLabel           labelInsert    = new JLabel("Enter Key to insert: ");
        JLabel           labelDelete    = new JLabel("Enter Key to delete: ");
        MyActionListener insertListener = new MyActionListener(true);
        MyActionListener deleteListener = new MyActionListener(false);

        mInsertField = new JTextField(8);
        mDeleteField = new JTextField(8);
        mInsertField.setMaximumSize(mInsertField.getPreferredSize());
        mDeleteField.setMaximumSize(mDeleteField.getPreferredSize());
        mInsertField.addActionListener(insertListener);
        mDeleteField.addActionListener(deleteListener);

        JPanel topPanel   = new JPanel();
        Box    firstPanel = Box.createHorizontalBox();
        topPanel  .setLayout(new BorderLayout());
        firstPanel.add(Box.createHorizontalGlue());
        firstPanel.add(labelInsert);
        firstPanel.add(mInsertField );
        firstPanel.add(Box.createHorizontalGlue());
        firstPanel.add(labelDelete );
        firstPanel.add(mDeleteField);
        firstPanel.add(Box.createHorizontalGlue());
        topPanel.add(firstPanel, BorderLayout.NORTH);
        TreePanel   panel      = new TreePanel(mTrie);
        JScrollPane scrollPane = new JScrollPane(panel);
        topPanel.add(scrollPane);
        add(topPanel);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }   

    public static void main(String[] args) {
        JFrame frame = new AnimationFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Listener for the text fields. Inserts or deletes keys.
    class MyActionListener implements ActionListener
    {
        // true if this listener should insert keys
        private boolean mInsert;
        
        MyActionListener(boolean insert) {
            mInsert = insert;
        }
        
        // Parse the number and insert or delete the key
        public void actionPerformed(ActionEvent e) {
            if (mInsert) {
                String key = mInsertField.getText().trim();
                mInsertField.setText(null);
                mTrie.insert(key, "itxyz");
            } else {
                String key = mDeleteField.getText().trim();
                mDeleteField.setText(null);
                mTrie.delete(key);
            }
            repaint();
        }
    }
}


