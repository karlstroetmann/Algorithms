import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Dijkstra extends JPanel
{
    public static void main(String[] args) 
    {
	JFrame jf = new JFrame("Dijkstra");
	Container b = jf.getContentPane();
	b.setLayout(new BorderLayout());
        jf.pack();
	jf.setSize(700,600);
        Dijkstra d = new Dijkstra();
	b.add(d, BorderLayout.CENTER);
        JPanel north = new JPanel();
        b.add(north, BorderLayout.NORTH);
        north.add(d.male);
        north.add(d.start);
        north.add(d.stop);
        d.setBackground(Color.black);
	
	jf.setVisible(true);
	
    }
    
    int x1 = 10;
    int x2 = 100;
    int y1 = 10;
    int y2 = 100;

    public void aendere() 
    {
	x1 = x1 + 1;
	y1 = y1 + 2;
	x2 = x2 + 3;
	y2 = y2 + 4;
	repaint();
    }
    

    
    JButton male = new JButton("Male");
    {
        male.addActionListener(
            new ActionListener() 
            {
		public void actionPerformed(ActionEvent e) 
		{
                    aendere();
		}	    
	    });
    }
    Thread th = null;
    boolean notStopped;
    
    JButton start = new JButton("Start");
    {
        start.addActionListener(
            new ActionListener() 
            {
		public void actionPerformed(ActionEvent e) 
		{
                    th = new Thread()
			{
			    public void run() 
			    {
                                notStopped = true;
                                while (notStopped) {
                                    try {
	        			    Thread.sleep(100);
		        		}
			        	catch (InterruptedException ee) {}
				    aendere();				
                                }
			    }
			};
		    th.start();                        
		}
	    });
        start.addKeyListener(
            new KeyAdapter() 
            {
		public void keyTyped(KeyEvent e) 
		{
		    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        th = new Thread()
			{
			    public void run() 
			    {
                                notStopped = true;
                                while (notStopped) {
                                    try {
	        			    Thread.sleep(100);
		        		}
			        	catch (InterruptedException ee) {}
				    aendere();				
                                }
			    }
			};
			th.start(); 
		    }
		}
	    });
    }


    JButton stop = new JButton("Stop");
    {
        stop.addActionListener(
            new ActionListener() 
            {
		public void actionPerformed(ActionEvent e) 
		{
                    notStopped = false;
		}
	    });
        stop.addKeyListener(
            new KeyAdapter() 
            {
		public void keyTyped(KeyEvent e) 
		{
                    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        notStopped = false;
		    }
		}
	    });
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
	g.setColor(Color.red);
	g.drawLine(x1,y1,x2,y2);
	g.setColor(Color.blue);
	g.drawOval(x1+x1,y1,x2+x2,y2);
	g.setColor(Color.magenta);
	g.drawString("Hallo " + notStopped,100,100);

    }
    
}
