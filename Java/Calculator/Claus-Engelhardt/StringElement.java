import java.awt.*;

public class StringElement extends GraphicElement {
	
	private String StrElement;
	
	StringElement() {
		super();
		StrElement = new String("_");
	}
	
	StringElement(Point p) {
		super(p);
	}
	
	StringElement(String str) {
		super();
		StrElement = new String(str);
	}
	
	StringElement(String str, Point p) {
		super(p);
		StrElement = new String(str);
	}
	
	public void setString(String str) {
		StrElement = str;
	}
	
	public int getStrLength() {
		return StrElement.length();
	}
	
	public void paintElement(Graphics g) {
		g.drawString(StrElement,getPosX(),getPosY());				
	}
	
	public String getString() {
		return StrElement;
	}

}
