//Animation frame
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.applet.*;


public class OutputGUI extends JFrame {
	  //Declaration of objetct variables
	  private JFrame frame;
	  private JPanel ControlPanel;
	  private OutputPanel GraphicPanel;
	  private int mWidth = 800;
	  private int mHigh = 600;
	  private boolean nextStep = false;
	  int speed = 1;
	  
	  //initalize Sound File
	  File f=new File("Song.wav");
	  AudioClip sound = null;
	  	  
	//Constructor
	OutputGUI() {
		//Initialize Frame and Panel
		frame = new JFrame();
		ControlPanel = new JPanel();
		GraphicPanel = new OutputPanel();
		
		//Initialize actionListener
		ActionListener pressButton = new pressedButton();
		 
		//Initialize new Buttons
		JButton bRun = new JButton("Run");
		bRun.addActionListener(pressButton);
		
		JButton bStep = new JButton("Step");
		bStep.addActionListener(pressButton);
		 
		 
		//Initialize new Frame Attributes
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		GraphicPanel.setSize(mWidth,mHigh);
		GraphicPanel.setBackground(Color.yellow);
		ControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 200,5));
		ControlPanel.add(bRun);
		ControlPanel.add(bStep);
		frame.add(ControlPanel, BorderLayout.NORTH);
		frame.add(GraphicPanel, BorderLayout.CENTER );
		frame.setBounds(200,50,800,600);
		frame.setVisible(false);
	}
	
	//Constructor to initialize window size 
	OutputGUI(int Width, int High) {
		this();
		frame.setSize(Width, High);

	}
	
	//Initialize InputList for graphical Output
	public void setInputList(Stack<Object>  InputList) {
		GraphicPanel.setInputList(InputList);
		//New Position for worker
		Point p = new Point();
		p = (Point) GraphicPanel.Eingabe.getLast().getPos().clone();
		p.x = calcUtil.relativXPosition(p.x,GraphicPanel.Eingabe.getLast());
		p.y -= 20;
		//Init worker
		GraphicPanel.worker = new workerElement(p);
		GraphicPanel.worker.work = false;

		GraphicPanel.repaint();
	}
	
	//deletes an element from InputList and add's it to the ArgumentList
	public void addArgumentfromInputList() {
		if( GraphicPanel.ArgumentList == null ){
			GraphicPanel.ArgumentList = new LinkedList<StringElement>();
		}
		
		StringElement str = GraphicPanel.Eingabe.removeFirst();
		
		GraphicPanel.ArgumentList.add(str);
		GraphicPanel.repaint();
		
		//Calculate new position 
		Point newPos = new Point(); 
		newPos.y = calcUtil.relativStackKoord(GraphicPanel.ArgListPos.y,
				                              GraphicPanel.ArgumentList);
		newPos.x = GraphicPanel.ArgListPos.x;
		
		moveElementToPos(str,newPos);
		
		if( !GraphicPanel.Eingabe.isEmpty() )
			moveInputList(str);
		
	}
	
	//Animate Elements
	private void moveElementToPos(Object o, Point pos) {
		GraphicElement gE = (GraphicElement) o;
		Point p = gE.getPos();
		while( !gE.getPos().equals(pos) ){
			gE.setPos(p);
			if(p.x>pos.x)
				p.x-=speed;
			if(p.x<pos.x)
				p.x+=speed;
			if(p.y>pos.y)
				p.y-=speed;
			if(p.y<pos.y)
				p.y+=speed;
			GraphicPanel.repaint();
			try{
				Thread.sleep(5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//Initialize new results
	public void addErgebnis(Double Erg, boolean LHS) {
		Point ErgPos = new Point(GraphicPanel.ErgebnisPos.x,
								 GraphicPanel.ErgebnisPos.y);
		
		GraphicPanel.Ergebnis = new StringElement(Erg.toString(),ErgPos);
		GraphicPanel.repaint();
		
		if(GraphicPanel.Eingabe.isEmpty() &&
			GraphicPanel.OperantList.size() == 1) {
			Calculator.setStepMode(false);
		}
		
		nextStep = false;
		if(Calculator.getStepMode()) {
			while(!nextStep); 
			
			nextStep = false;
		}else {
			try{
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(GraphicPanel.Eingabe.size() > 0) {
			if(GraphicPanel.Eingabe.getFirst().getString().equals(")") &&
				GraphicPanel.OperantList.size() == 2 &&
				GraphicPanel.Eingabe.size() == 1) {
					
				GraphicPanel.Eingabe.removeLast();
				GraphicPanel.OperantList.removeFirst();
				GraphicPanel.repaint();
			}
		}
				
		if(!GraphicPanel.Eingabe.isEmpty() || 
		   GraphicPanel.OperantList.size() != 1) {
				addResultToArgList(LHS);
		}else {
			//stop sound
			//sound.stop();
			//happy worker
			GraphicPanel.worker.happy = true;
			while(true) {
				GraphicPanel.repaint();
				try {
					Thread.sleep(500);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
			
	}
	
	//Add result to ArgumentList
	public void addResultToArgList(boolean LHS) {
		if( GraphicPanel.ArgumentList == null ){
			GraphicPanel.ArgumentList = new LinkedList<StringElement>();
		}
		//Delete the calculated arguments and operants
		GraphicPanel.ArgumentList.removeLast();
		if( LHS )
			 GraphicPanel.ArgumentList.removeLast();
		GraphicPanel.OperantList.removeLast();
		
		//add result to ArgList as new Element
		StringElement str = GraphicPanel.Ergebnis;
		
		GraphicPanel.ArgumentList.add(str);
		GraphicPanel.Ergebnis = null;
		GraphicPanel.repaint();
		
		//Calculate new position 
		Point newPos = new Point(); 
		newPos.y = calcUtil.relativStackKoord(GraphicPanel.ArgListPos.y,
				                              GraphicPanel.ArgumentList);
		newPos.x = GraphicPanel.ArgListPos.x;
		
		moveElementToPos(str,newPos);
	}
	
	public void addOperantfromInputList(String operant) {
		if( GraphicPanel.OperantList == null ){
			GraphicPanel.OperantList = new LinkedList<StringElement>();
		}
		
		if( GraphicPanel.Eingabe.isEmpty() )
			return;
		
		StringElement str = GraphicPanel.Eingabe.removeFirst();
		
		GraphicPanel.OperantList.add(str);
		GraphicPanel.repaint();
		
		//Calculate new position 
		Point newPos = new Point(); 
		newPos.y = calcUtil.relativStackKoord(GraphicPanel.OpListPos.y,
				                              GraphicPanel.OperantList);
		newPos.x = GraphicPanel.OpListPos.x;
		
		moveElementToPos(str,newPos);
		
		if( !GraphicPanel.Eingabe.isEmpty() )
			moveInputList(str);
		
		//Klammern entfernen
		if( str.getString().equals(")")) {
			GraphicPanel.OperantList.removeLast();
			GraphicPanel.OperantList.removeLast();
		}
		GraphicPanel.repaint();
	}
	
	//Move InputList to left
	public void moveInputList(StringElement str) {
		int xDiff = calcUtil.relativXPosition(str.getPosX(),str) - str.getPosX();
		GraphicPanel.worker.work = true;
		for(int i=0; i<=xDiff;i++) {
			for(StringElement strE: GraphicPanel.Eingabe) {
				Point newP = strE.getPos();
				newP.x--; 
				strE.setPos(newP);
			}
			
			//New Position for worker
			if( !GraphicPanel.Eingabe.isEmpty() ) {
				Point p = new Point();
				p = (Point) GraphicPanel.Eingabe.getLast().getPos().clone();
				p.x = calcUtil.relativXPosition(p.x,GraphicPanel.Eingabe.getLast());
				p.y -= 20;
				GraphicPanel.worker.setPos(p);
			}
			
			GraphicPanel.repaint();
			try {
				Thread.sleep(10);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		GraphicPanel.worker.work = false;
	}
	
	//moves the next Argument which are to calculate
	public void moveArgumentToCalculate(boolean RHS) {
		Point newPos = new Point();
		newPos = (Point) GraphicPanel.ErgebnisPos.clone();
		newPos.y = calcUtil.relativStackKoord(GraphicPanel.ErgebnisPos.y,2);
		
		if ( RHS ) {
			newPos.x = calcUtil.relativXPosition(newPos.x,GraphicPanel.OperantList.getLast());
			newPos.y = calcUtil.relativStackKoord(GraphicPanel.ErgebnisPos.y,2);
			moveElementToPos(GraphicPanel.ArgumentList.getLast(),newPos);
		} else {
			
			int diff = calcUtil.relativXPosition(newPos.x,GraphicPanel.ArgumentList.get(
					GraphicPanel.ArgumentList.size()-2)) - GraphicPanel.ErgebnisPos.x;
			
			newPos.x -= diff;
			
			moveElementToPos(GraphicPanel.ArgumentList.get(GraphicPanel.ArgumentList.size()-2),newPos);
		}
		
	}
	
	public void moveOperantToCalculate() {			
		Point newPos = new Point();
		newPos = (Point) GraphicPanel.ErgebnisPos.clone();
		newPos.y = calcUtil.relativStackKoord(GraphicPanel.ErgebnisPos.y,2);
		moveElementToPos(GraphicPanel.OperantList.getLast(),newPos);
	}
	
	private class pressedButton implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {
			String strPressedButton = event.getActionCommand();
			
			if( strPressedButton == "Run" ) {
				if(Calculator.getStepMode()) {
					Calculator.setStepMode(false);
				} else
					nextStep = true;
			}
			
			if( strPressedButton == "Step")
				if( !Calculator.getStepMode() ) {
					Calculator.setStepMode(true);
				}else
					nextStep = true;
		}
      
	}
		
	public void Visible(boolean visible) {
		frame.setVisible(visible);
		//start or stop sound
		if(visible) {
			try{
				sound = Applet.newAudioClip( f.toURL() );
			} catch (Exception e) {
				e.printStackTrace();
			}
			sound.loop();
		} else {
			sound.stop();
		}
	}

}
