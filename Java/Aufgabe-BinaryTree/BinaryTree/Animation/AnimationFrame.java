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
    private BinaryTree<Integer, String> mBinaryTree;

    TreePanel(BinaryTree<Integer, String> binaryTree) {
        mBinaryTree = binaryTree;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        setBackground(new Color(0.8F, 0.8F, 1.0F));
        super.paintComponent(g);
		Node root = mBinaryTree.mRoot;
		root.draw(0.0, 0.0);
        paintNode(g2, root);
        Dimension dimension = 
            new Dimension((int) root.getWidth(), (int) root.getHeight());
        setPreferredSize(dimension);
    }
    
    private void paintNode(Graphics2D g2, Node node) {
        Point2D   top    = node.mTop;
        double    topX   = top.getX();
        double    topY   = top.getY();
        if (node.isEmpty()) {
            Ellipse2D circle = 
                new Ellipse2D.Double(topX - Node.sSmallRadius, topY,
                                     Node.sSmallDiameter, Node.sSmallDiameter);
            g2.setPaint(Color.BLACK);
            g2.fill(circle);
            return;
        }
        Ellipse2D circle = 
            new Ellipse2D.Double(topX - Node.sRadius, topY,
                                 Node.sDiameter, Node.sDiameter);
        double key   = ((Integer) node.getKey()).doubleValue() * 0.01;      
        double red   = 2.0 * abs(key - 0.5);
        double green = Math.cos(key * 0.5 * PI);
        double blue  = 0;
        g2.setPaint(new Color((float) red, (float) green, (float) blue));
        g2.fill(circle);
        g2.setPaint(new Color(0.0F, 0.0F, 1.0F));
        g2.drawString(node.getKey().toString(), 
                      (int) (topX - 0.5 * Node.sRadius), 
                      (int) (topY + 1.3 * Node.sRadius));

        paintNode(g2, node.getLeft());
        paintNode(g2, node.getRight());
        double  radius2   = Node.sRadius / Math.sqrt(2);
        Point2D topLeft   = node.getLeft ().mTop;
        Point2D topRight  = node.getRight().mTop;
        int     leftX     = (int) (topLeft. getX());
        int     rightX    = (int) (topRight.getX());
        int     leftY     = (int) topLeft. getY();
        int     rightY    = (int) topRight.getY();
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

    BinaryTree<Integer, String> mTree;
    JTextField                  mInsertField;
    JTextField                  mDeleteField;

    /** Get the number of values to be sorted. */
    public AnimationFrame() {
        Random random = new Random(1);
        mTree = new BinaryTree<Integer, String>();
        for (int i = 0; i < 18; ++i) {
            mTree.insert(random.nextInt(100), "a");
        }

        JLabel           labelInsert    = new JLabel("Enter Key to insert: ");
        JLabel           labelDelete    = new JLabel("Enter Key to delete: ");
		MyActionListener insertListener = new MyActionListener(true);
		MyActionListener deleteListener = new MyActionListener(false);

        mInsertField = new JTextField(3);
        mDeleteField = new JTextField(3);
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
        TreePanel   panel      = new TreePanel(mTree);
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
				Integer key = Integer.parseInt(mInsertField.getText().trim());
				mInsertField.setText(null);
				mTree.insert(key, "b");
			} else {
				Integer key = Integer.parseInt(mDeleteField.getText().trim());
				mDeleteField.setText(null);
				mTree.delete(key);
				System.out.println(mTree);
			}
			repaint();
		}
	}
}


