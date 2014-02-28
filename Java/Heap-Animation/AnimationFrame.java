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
    private HeapTree<Integer, Integer> mHeapTree;

    TreePanel(HeapTree<Integer, Integer> heapTree) {
        mHeapTree = heapTree;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        setBackground(new Color(0.8F, 0.8F, 1.0F));
        super.paintComponent(g);
        HeapNode<Integer, Integer> root = mHeapTree.mRoot;
        root.draw(0.0, 0.0);
        paintNode(g2, root);
        Dimension dimension = 
            new Dimension((int) root.getWidth(), (int) root.getHeight());
        setPreferredSize(dimension);
    }
    
    private void paintNode(Graphics2D g2, HeapNode<Integer, Integer> node) {
        Point2D   top    = node.mTop;
        double    topX   = top.getX();
        double    topY   = top.getY();
        if (node.isEmpty()) {
            Ellipse2D circle = 
                new Ellipse2D.Double(topX - HeapNode.sSmallRadius, topY,
                                     HeapNode.sSmallDiameter, HeapNode.sSmallDiameter);
            g2.setPaint(Color.BLACK);
            g2.fill(circle);
            return;
        }
        Ellipse2D circle = 
            new Ellipse2D.Double(topX - HeapNode.sRadius, topY,
                                 HeapNode.sDiameter, HeapNode.sDiameter);
        double key   = ((Integer) node.getKey()).doubleValue() * 0.01;      
        double red   = min(2.0 * abs(key - 0.5), 1.0);
        double green = min(abs(cos(key * 0.5 * PI)),  1.0);
        double blue  = 0;
        g2.setPaint(new Color((float) red, (float) green, (float) blue));
        g2.fill(circle);
        g2.setPaint(new Color(0.0F, 0.0F, 1.0F));
        g2.drawString(node.getKey().toString(), 
                      (int) (topX - 0.5 * HeapNode.sRadius), 
                      (int) (topY + 1.3 * HeapNode.sRadius));

        paintNode(g2, node.getLeft());
        paintNode(g2, node.getRight());
        double  radius2   = HeapNode.sRadius / Math.sqrt(2);
        Point2D topLeft   = node.getLeft ().mTop;
        Point2D topRight  = node.getRight().mTop;
        int     leftX     = (int) (topLeft. getX());
        int     rightX    = (int) (topRight.getX());
        int     leftY     = (int) topLeft. getY();
        int     rightY    = (int) topRight.getY();
        int     topXleft  = (int) (topX - radius2);
        int     topXright = (int) (topX + radius2);
        int     topYleft  = (int) (topY + HeapNode.sRadius + radius2);
        int     topYright = (int) (topY + HeapNode.sRadius + radius2);
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

    HeapTree<Integer, Integer>       heapTree;
    BinaryHeapNode<Integer, Integer> mNode;        // last node inserted
    JTextField                       mInsertField;
    JTextField                       mChangeField;
    JButton                          mRemoveButton;

    /** Get the number of values to be sorted. */
    public AnimationFrame() {
        mNode = null;
        Random random = new Random(1);
        heapTree = new HeapTree<Integer, Integer>();
        for (int i = 0; i < 12; ++i) {
            mNode = heapTree.insert(random.nextInt(100), 42);
        }

        JLabel               labelInsert    = new JLabel("Key to insert: ");
        JLabel               labelChange    = new JLabel("New Priority: ");
        InsertRemoveListener insertListener = new InsertRemoveListener(true);
        ChangeListener       changeListener = new ChangeListener();
        InsertRemoveListener removeListener = new InsertRemoveListener(false);

        mInsertField  = new JTextField(3);
        mChangeField  = new JTextField(3);
        mRemoveButton = new JButton("Remove");
        mInsertField.setMaximumSize(mInsertField.getPreferredSize());
        mInsertField.addActionListener(insertListener);
        mChangeField.setMaximumSize(mChangeField.getPreferredSize());
        mChangeField.addActionListener(changeListener);
        mRemoveButton.addActionListener(removeListener);

        JPanel topPanel   = new JPanel();
        Box    firstPanel = Box.createHorizontalBox();
        topPanel  .setLayout(new BorderLayout());
        firstPanel.add(Box.createHorizontalGlue());
        firstPanel.add(labelInsert);
        firstPanel.add(mInsertField );
        firstPanel.add(Box.createHorizontalGlue());
        firstPanel.add(mRemoveButton);
        firstPanel.add(Box.createHorizontalGlue());
        firstPanel.add(labelChange);
        firstPanel.add(mChangeField );
        firstPanel.add(Box.createHorizontalGlue());
        topPanel.add(firstPanel, BorderLayout.NORTH);
        TreePanel   panel      = new TreePanel(heapTree);
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

    // Listener for the insert text field and he remove button. Inserts or removes keys.
    class InsertRemoveListener implements ActionListener
    {
        // true if this listener should insert keys
        private boolean mInsert;
        
        InsertRemoveListener(boolean insert) {
            mInsert = insert;
        }
        
        // Parse the number and insert or delete the key
        public void actionPerformed(ActionEvent e) {
            if (mInsert) {
                Integer key = Integer.parseInt(mInsertField.getText().trim());
                mInsertField.setText(null);
                mNode = heapTree.insert(key, 0);
            } else {
                heapTree.remove();
            }
            repaint();
        }
    }

    // Listener for the change text field.  Changes keys.
    class ChangeListener implements ActionListener
    {
        // Parse the number and insert or delete the key
        public void actionPerformed(ActionEvent e) {
            Integer key = Integer.parseInt(mChangeField.getText().trim());
            mChangeField.setText(null);
            mNode.change(key);
            repaint();
        }
    }
}


