import java.awt.*;

abstract class GraphicElement {
	 private Point Position;
	
	GraphicElement() {
		Position = new Point();
		Position.x=0;
		Position.y=0;
	}
	
	GraphicElement(Point p) {
		Position = new Point();
		try {
			Position = (Point) p.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	GraphicElement(int x, int y) {
		Position = new Point();
		Position.x = x;
		Position.y = y;
	}	
	
	public int getPosX() {
		return Position.x;
	}
	
	public int getPosY() {
		return Position.y;
	}
	
	public Point getPos() {
		return Position;
	}
	
	public void setPos(Point p) {
		Position = p;
	}
	
	abstract public void paintElement(Graphics g);
}
