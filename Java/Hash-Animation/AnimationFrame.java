import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import javax.swing.event.*;

import static java.lang.Math.*;

class HashPanel extends JPanel
{
    static final double sBoxSize = 45;
    static final double sOffset  = 10;
    
    private MyHashMap<String, String> mHashMap = null;

    HashPanel(MyHashMap<String, String> hashMap) {
        mHashMap = hashMap;
    }

    // Paint the hash map.
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        setBackground(new Color(0.8F, 0.8F, 1.0F));
        super.paintComponent(g);
        int maxLength = 0;
        double maxX = 100;
        double maxY = 100;
        for (int i = 0; i < mHashMap.mArray.length; ++i) {
            LinkedList<Pair<String, String>> list = mHashMap.mArray[i];
            if (list != null) {
                int size = list.size();
                maxLength = max(maxLength, size);
                double xOffset = 5 * sOffset + sBoxSize;
                double yOffset = (i + 1) * (sBoxSize + sOffset);
                drawArrow(g2, xOffset - 5 * sOffset, yOffset + 0.5 * sBoxSize, 
                              xOffset, yOffset + 0.5 * sBoxSize);
                for (int k = 0; k < size; ++k) {
                    String key   = list.get(k).getFirst();
                    String value = list.get(k).getSecond();
                    xOffset += drawBoxWithKey(g2, xOffset, yOffset, key, value);
                    drawArrow(g2, xOffset, yOffset + 0.5 * sBoxSize, 
                              xOffset + 3 * sOffset, yOffset + 0.5 * sBoxSize);
                    xOffset += 3 * sOffset;
                    maxX = xOffset + 3 * sOffset;
                }
                double radius = 0.6 * sOffset;
                Ellipse2D dot = 
                    new Ellipse2D.Double(xOffset, 
                                         yOffset - radius + 0.5 * sBoxSize,
                                         2 * radius, 
                                         2 * radius);
                g2.setPaint(Color.BLACK);
                g2.fill(dot);
            }
            double      yOffset = (i + 1) * (sBoxSize + sOffset);
            Rectangle2D box     = 
                new Rectangle2D.Double(sOffset, yOffset, sBoxSize, sBoxSize);
            g2.setPaint(Color.YELLOW);
            g2.fill(box);
            g2.setPaint(Color.BLUE);
            g2.draw(box);
            g2.setPaint(Color.BLACK);
            drawBoxWithInt(g2, sOffset, yOffset, i);
            maxY = yOffset + sBoxSize + sOffset;
        }
        Dimension dimension = new Dimension((int) (maxX), (int) (maxY));
        setPreferredSize(dimension);
    }

    // Draw the integer number centered in a box whose upper left coordinate is (x,y).
    private void drawBoxWithInt(Graphics2D g2, double x, double y, Integer number)
    {
        Font              sansSerif = new Font("SansSerif", Font.PLAIN, 20);
        g2.setFont(sansSerif);
        String            numberStr = number.toString();
        FontRenderContext context   = g2.getFontRenderContext();
        Rectangle2D       bound     = sansSerif.getStringBounds(numberStr, context);
        double            xOffset   = 0.5 * (sBoxSize - bound.getWidth());
        double            yOffset   = 0.5 * (sBoxSize - bound.getHeight()) - bound.getY();
        g2.drawString(numberStr, (int) (x + xOffset), (int) (y + yOffset));
    }
    
     // Draw a box with key and value at position (x,y).
     private double drawBoxWithKey(Graphics2D g2, 
                                 double x, double y, 
                                 String key, String value)
     {
         Font              serif1     = new Font("Serif", Font.ITALIC, 12);
         g2.setFont(serif1);
         FontRenderContext context1   = g2.getFontRenderContext();
         Rectangle2D       boundKey   = serif1.getStringBounds(key, context1);
        double            width1     = boundKey.getWidth() + sOffset;

         Font              serif2     = new Font("Serif", Font.PLAIN, 12);
         g2.setFont(serif2);
         FontRenderContext context2   = g2.getFontRenderContext();
         Rectangle2D       boundValue = serif2.getStringBounds(value, context2);
        double            width2     = boundValue.getWidth() + sOffset;
        double            width      = Math.max(width1, width2);

         double            xOffset1   = 0.5 * (width - boundKey.getWidth());
         double            yOffset1   = 0.5 * (0.5 * sBoxSize  - boundKey.getHeight()) - boundKey.getY();
        Rectangle2D       box        = new Rectangle2D.Double(x, y, width, sBoxSize);
         double            xOffset2   = 0.5 * (width - boundValue.getWidth());
         double            yOffset2   = 0.5 * (0.5 * sBoxSize  - boundValue.getHeight()) - boundValue.getY();
        g2.setPaint(Color.ORANGE);
        g2.fill(box);
        g2.setPaint(Color.BLUE);
        g2.draw(box);
        g2.setPaint(Color.BLACK);
         g2.drawString(key, (int) (x + xOffset1), (int) (y + yOffset1));
        g2.drawLine((int) x, (int) (y + 0.5 * sBoxSize), 
                    (int) (x + width), (int) (y + 0.5 * sBoxSize));

        g2.setFont(serif2);
         g2.drawString(value, (int) (x + xOffset2), (int) (y + 0.5 * sBoxSize + yOffset2));

        return width;
     }
    
    private void drawArrow(Graphics2D g2, double x1, double y1, double x2, double y2) {
        int i1 = (int) x1;
        int j1 = (int) y1;
        int i2 = (int) x2; 
        int j2 = (int) y2;
        g2.drawLine(i1, j1, i2, j2);
        g2.drawLine(i2 - (int) sOffset, j2 - (int) sOffset, i2, j2);
        g2.drawLine(i2 - (int) sOffset, j2 + (int) sOffset, i2, j2);
    }

}


/** This frame shows the array as it is sorted, together with buttons to single-step the animation
    or to run it without interruption.
*/
public class AnimationFrame extends JFrame
{
    private static final int DEFAULT_WIDTH  = 800;
    private static final int DEFAULT_HEIGHT = 800;

    MyHashMap<String, String> mHashMap;
    JTextField                mInsertField;
    JTextField                mDeleteField;
    private Wrap<Boolean>     mPause;

    /** Get the number of values to be sorted. */
    public AnimationFrame() {
        setTitle("Animation of a HashMap");
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
        mPause = new Wrap<Boolean>();
        mPause.setValue(true);
        JButton       pauseButton   = new JButton("Pause");
        PauseListener pauseListener = new PauseListener(mPause, pauseButton);
        pauseButton.addActionListener(pauseListener);

        JPanel topPanel   = new JPanel();
        Box    firstPanel = Box.createHorizontalBox();
        topPanel  .setLayout(new BorderLayout());
        firstPanel.add(Box.createHorizontalGlue());
        firstPanel.add(pauseButton);
        firstPanel.add(Box.createHorizontalGlue());
        firstPanel.add(labelInsert);
        firstPanel.add(mInsertField );
        firstPanel.add(Box.createHorizontalGlue());
        firstPanel.add(labelDelete );
        firstPanel.add(mDeleteField);
        firstPanel.add(Box.createHorizontalGlue());
        topPanel.add(firstPanel, BorderLayout.NORTH);

        LinkedList<Pair<String, String>>[] dummyArray = (LinkedList<Pair<String, String>>[]) new LinkedList[0];
        mHashMap = new MyHashMap<String, String>(0,dummyArray);

        HashPanel   panel      = new HashPanel(mHashMap);
        JScrollPane scrollPane = new JScrollPane(panel);
        topPanel.add(scrollPane);
        add(topPanel);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        ArrayList<Pair<String, String>> all = new ArrayList<Pair<String, String>>();

        all.add(new Pair<String, String>("Dominik Greibl", "ai0"));
        all.add(new Pair<String, String>("Oliver Aehle", "ai1"));
        all.add(new Pair<String, String>("Michael Neuhold", "ai2"));
        all.add(new Pair<String, String>("Alexander Busse", "ai3"));
        all.add(new Pair<String, String>("Wolfgang JŠger", "ai4"));
        all.add(new Pair<String, String>("Steffen Gei§inger", "ai5"));
        all.add(new Pair<String, String>("Philipp Boss", "ai6"));
        all.add(new Pair<String, String>("Claudia Schwarzenberger", "ai7"));
        all.add(new Pair<String, String>("Jan Kraus", "ai8"));
        all.add(new Pair<String, String>("Tarek Hamed", "ai9"));
        all.add(new Pair<String, String>("Stefan Ruzitschka", "ai10"));
        all.add(new Pair<String, String>("Niels Krause", "ai11"));
        all.add(new Pair<String, String>("Jakob Anzenberger", "ai12"));
        all.add(new Pair<String, String>("Guido Schweizer", "ai13"));
        all.add(new Pair<String, String>("Norbert Kurz", "ai14"));
        all.add(new Pair<String, String>("Maja Neumann", "ai15"));
        all.add(new Pair<String, String>("Stephanie Schaaf", "ai16"));
        all.add(new Pair<String, String>("Johannes Unterstein", "ai17"));
        all.add(new Pair<String, String>("Tobias Lingenmann", "ai18"));
        all.add(new Pair<String, String>("Andreas Skibicki", "ai19"));
        all.add(new Pair<String, String>("Christian Schirmer", "ai20"));
        all.add(new Pair<String, String>("Marian Pšschmann", "ai21"));
        all.add(new Pair<String, String>("Jonas Herkommer", "ai22"));
        all.add(new Pair<String, String>("Matthias Gšckert", "ai23"));
        all.add(new Pair<String, String>("Florian Biesinger", "ai24"));

        int i = 0;
        while (i < all.size()) {
            String name  = all.get(i).getFirst();
            String value = all.get(i).getSecond();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!mPause.getValue().booleanValue()) {
                mPause.setValue(true);
                repaint();
                mHashMap.insert(name, value);
                ++i;
            }
        }
    }   

    public static void main(String[] args) {
        JFrame frame = new AnimationFrame();
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
                mHashMap.insert(key, "ai06123");
            } else {
                String key = mDeleteField.getText().trim();
                mDeleteField.setText(null);
                mHashMap.delete(key);
            }
            repaint();
        }
    }
}

class PauseListener implements ActionListener
{
    private Wrap<Boolean> mFlag;
    private JButton       mButton;  // the associated button
    
    PauseListener(Wrap<Boolean> flag, JButton button) {
        mFlag   = flag;
        mButton = button;
    }
    public void actionPerformed(ActionEvent e) {
        Boolean flag = mFlag.getValue();
        mFlag.setValue(!flag.booleanValue());
        if (flag) {
            mButton.setText("Pause");
        } else {
            mButton.setText("Run");
        }
    }
}
