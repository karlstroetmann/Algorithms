import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class AnimationFrame {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() 
        {
	    public void run() {
		DrawFrame frame = new DrawFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	    }
        });
    }
}

class DrawFrame extends JFrame {
    public DrawFrame() {
	int startSize = 800;
	setSize(startSize, startSize);
        setTitle("Monte-Carlo-Simulation zur Berechnung von pi");
        JButton button = new JButton("press to add point");
	int size = (int) (getSize().getWidth() * 0.4);
        setSize(2 * size + 50, 2 * size + 110);
        JPanel panel  = new JPanel();
        JButton result = new JButton("Pi = ?");
        MonteCarlo m = new MonteCarlo(size, result);
        button.addActionListener(m);
        panel.setLayout(new BorderLayout());
        panel.add(button, BorderLayout.NORTH);
        panel.add(m, BorderLayout.CENTER);
        panel.add(result, BorderLayout.SOUTH);
        add(panel);
    }

}

class MonteCarlo extends JPanel implements ActionListener {
    private int                    mOffset = 10; 
    private int                    mSize;           // size of window
    private JButton                mResult;         // label giving the result
    private java.util.List<Double> mXCoordinates;   // x-coordinates of the points
    private java.util.List<Double> mYCoordinates;   // y-coordinates of the points
    private Random                 mRandom;
    private int                    mTotal;          // number of all points
    private int                    mCount;          // number of points in circle
    
    public MonteCarlo(int size, JButton result) {
        mSize   = size;
        mResult = result;
        mRandom = new Random();
        mXCoordinates = new LinkedList<Double>();
        mYCoordinates = new LinkedList<Double>();
    }
    public void paintComponent(Graphics g) {
	mSize = (int) (getSize().getWidth() * 0.46);
        Graphics2D  g2     = (Graphics2D) g;
        Rectangle2D square = new Rectangle2D.Double(mOffset, mOffset, 
                                                    2 * mSize + mOffset, 2 * mSize + mOffset);
        Ellipse2D   circle = new Ellipse2D.Double();
        setBackground(Color.WHITE);
        circle.setFrame(square);
        g2.draw(circle);
        g2.draw(square);
        g2.setPaint(new Color(240, 240, 100));
        g2.fill(circle);
        g2.setPaint(Color.RED);
        if (mXCoordinates.size() > 0) {
            Double xPosition = mSize * (1 + mXCoordinates.get(0)) + mOffset;
            Double yPosition = mSize * (1 - mYCoordinates.get(0)) + mOffset;        
            System.out.printf("(%g, %g)\n", xPosition, yPosition);
            Rectangle2D point = new Rectangle2D.Double(xPosition, yPosition, 2, 2);
            g2.fill(point);
        }
        g2.setPaint(Color.BLUE);
        for (int i = 1; i < mXCoordinates.size(); ++i) {
            Double xPosition = mSize * (1 + mXCoordinates.get(i)) + mOffset;
            Double yPosition = mSize * (1 - mYCoordinates.get(i)) + mOffset;        
            System.out.printf("(%g, %g)\n", xPosition, yPosition);
            Rectangle2D point = new Rectangle2D.Double(xPosition, yPosition, 2, 2);
            g2.fill(point);
        }
    }

    public void actionPerformed(ActionEvent e) {
	for (int k = 0; k < 100; ++k) {
	    Double x = 2 * mRandom.nextDouble() - 1.0;
	    Double y = 2 * mRandom.nextDouble() - 1.0;
	    mXCoordinates.add(0, x);
	    mYCoordinates.add(0, y);
	    ++mTotal;
	    if (x * x + y * y <= 1.0) {
		++mCount;
	    }
	    String xs = x.toString().substring(0, 5);
	    String ys = y.toString().substring(0, 5);
	    Double pi = 4.0 * mCount / mTotal;
	    String rs = pi + "    ";
	    String s  = rs.substring(0, 6);
	    mResult.setText("(" + xs + ", " + ys + ")      Number of all Points = " + mTotal + 
			    ", number of points inside circle = " + mCount +
			    ", Pi = " + s);
	}
	repaint();
    }
}