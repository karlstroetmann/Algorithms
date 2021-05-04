//wokerElement Class
//Animates the worker on the Top of the page

import java.awt.*;

public class workerElement extends GraphicElement{
	
	boolean work = false;
	boolean happy = false;
	
	private boolean up = true;
	
	private int Headsize = 6;
	private int Bodysize = 15;
	private int Legsize =  10;
	private int Armsize =  6;
	
	workerElement() {
		super();
	}
	
	workerElement(Point p) {
		super(p);
	}
	
	private void paintnormal(Graphics g) {
		g.setPaintMode();
		g.setColor(Color.black);
		//Kopf
		g.drawOval(getPosX()-3,getPosY(),Headsize,Headsize);
		//Body
		g.drawLine(getPosX(),getPosY()+ Headsize,getPosX(),getPosY()+Bodysize);
		//Legs
		g.drawLine(getPosX(),getPosY()+ Bodysize,getPosX()+4,getPosY()+Bodysize+Legsize);
		g.drawLine(getPosX(),getPosY()+ Bodysize,getPosX()-4,getPosY()+Bodysize+Legsize);
		//Arms
		if( work ) {
			g.drawLine(getPosX(),getPosY()+Headsize + 3,
					   getPosX()-Armsize,getPosY()+Headsize +3);
			g.drawLine(getPosX(),getPosY()+Headsize + 5,
					   getPosX()-Armsize,getPosY()+Headsize +5);
		} else {
			g.drawLine(getPosX(),getPosY()+Headsize +3,
					   getPosX()+Armsize,getPosY()+Headsize +5);
			g.drawLine(getPosX(),getPosY()+Headsize +3,
					   getPosX()-Armsize,getPosY()+Headsize +5);
		}
	}
	
	private void painthappy(Graphics g) {
		if(up) {
			g.setPaintMode();
			g.setColor(Color.black);
			//Kopf
			g.drawOval(getPosX()-3,getPosY(),Headsize,Headsize);
			//Body
			g.drawLine(getPosX(),getPosY()+ Headsize,getPosX(),getPosY()+Bodysize);
			//Legs
			//g.drawLine(getPosX(),getPosY()+ Bodysize,getPosX()+6,getPosY()+Bodysize+6);
			//g.drawLine(getPosX(),getPosY()+ Bodysize,getPosX()-6,getPosY()+Bodysize+6);
			g.drawLine(getPosX(),getPosY()+ Bodysize,getPosX()+4,getPosY()+Bodysize+Legsize);
			g.drawLine(getPosX(),getPosY()+ Bodysize,getPosX()-4,getPosY()+Bodysize+Legsize);
			
			//Arms
			g.drawLine(getPosX(),getPosY()+Headsize +5,
					   getPosX()-Armsize-2,getPosY()+Headsize -2);
			
			g.drawLine(getPosX(),getPosY()+Headsize +5,
					   getPosX()+Armsize+2,getPosY()+Headsize -2);
		}else {
			paintnormal(g);
		}
		up = !up ;		
	}
	
	
	public void paintElement(Graphics g) {
		if(happy) {
			painthappy(g);
		} else {
			paintnormal(g);
		}
	}

}
