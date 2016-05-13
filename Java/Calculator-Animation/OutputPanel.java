//Animation panel
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class OutputPanel extends JPanel {
	
	LinkedList<StringElement> Eingabe;
	LinkedList<StringElement> ArgumentList;
	LinkedList<StringElement> OperantList;
	StringElement Ergebnis = null;
	workerElement worker = null;
	
	final Point EingabeStartPos = new Point(300,30);
	final Point ErgebnisPos = new Point(390,300);
	final Point ArgListPos = new Point(200,500);
	final Point OpListPos = new Point(600,500);
	
	OutputPanel() {
		Eingabe = null;
		ArgumentList = null;
		OperantList = null;
	}
	
	//Initalize InputList
	public void setInputList(Stack<Object> InputList) {
		Point newPos = new Point();
		newPos = (Point) EingabeStartPos.clone();
		Eingabe = new LinkedList<StringElement>();
		Stack<Object> IList = new ArrayStack<Object>();
		try {
		  IList = InputList.clone();	
		} catch (Exception e) {
			e.printStackTrace();
		}
		while(!IList.isEmpty()) {
			if( IList.top() instanceof Double) {
				Eingabe.add(new StringElement(Double.toString((Double) IList.top()), newPos));
				IList.pop();
				newPos.x = calcUtil.relativXPosition(newPos.x,
													 Eingabe.getLast());
			} else {
				Eingabe.add(new StringElement((String) IList.top(),newPos));
				IList.pop();
				newPos.x = calcUtil.relativXPosition(newPos.x,
													 Eingabe.getLast());
			}
		}
	}
	
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Initialize Font
		g.setFont(new Font("Serif", Font.ITALIC, 18));
		
		StringElement str;
		g.setColor(Color.black);
		
		//Output from the Input
		g.drawString("Eingabeliste: ",EingabeStartPos.x - 125,EingabeStartPos.y);
		if( Eingabe != null ){
			for(int i = 0; i < Eingabe.size(); i++) {
				str = Eingabe.get(i);
				str.paintElement(g);
			}
		}
		
		//Output Worker
		if(worker != null)
			worker.paintElement(g);
		
		
		//Output from the ArgumentList
		g.setColor(Color.black);
		g.drawString("Argument - Stack",ArgListPos.x - 50,ArgListPos.y + 20);
		g.setColor(Color.blue);
		if( ArgumentList != null ) {
		   for(int i = 0; i < ArgumentList.size(); i++) {
			   str = ArgumentList.get(i);
			   str.paintElement(g);			   
		   }
		}
		
		//Output form the OperantList
		g.setColor(Color.black);
		g.drawString("Operant - Stack",OpListPos.x - 50,OpListPos.y + 20);
		g.setColor(Color.red);
		if( OperantList != null ) {
			for(int i = 0; i < OperantList.size(); i++) {
				   str = OperantList.get(i);
				   str.paintElement(g);			   
			   }
		}
		
		//Output from the Result
		g.setColor(new Color(10,180,10));
		if( Ergebnis != null ) {
			int yPos = calcUtil.relativStackKoord(ErgebnisPos.y,1);
			g.drawString("=",ErgebnisPos.x,yPos);
			Ergebnis.paintElement(g);
		}
				
	}

}

	