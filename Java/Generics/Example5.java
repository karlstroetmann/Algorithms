import java.util.*; 

abstract class Shape {
    public abstract void draw(Canvas c);
}

class Circle extends Shape {
    private int x, y, radius;
    public void draw(Canvas c) { 
        // implementation
    }
}

class Rectangle extends Shape {
    private int x, y, width, height;
    public void draw(Canvas c) { 
        // implementation
    }
}

class Canvas {
    public void draw(Shape s) {
        s.draw(this);
    }
}


public class Example5 {

    public static void drawAll1(List<Shape> shapes, Canvas c) {
        for (Shape s: shapes) {
            s.draw(c);
        }
    }

    public static void drawAll2(List<? extends Shape> shapes, Canvas c) {
        for (Shape s: shapes) {
            s.draw(c);
        }
    }

    public static <T extends Shape> void drawAll3(List<T> shapes, Canvas c) {
        for (T s: shapes) {
            s.draw(c);
        }
    }


    public static void main(String[] args) {
        Canvas      c  = new Canvas();
        List<Shape> sl = new LinkedList<Shape>();
        sl.add(new Circle());
        sl.add(new Rectangle());
        drawAll1(sl, c);
        List<Circle> cl = new LinkedList<Circle>();
        cl.add(new Circle());
        cl.add(new Circle());
        // drawAll1(cl, c);  // does not work
        drawAll2(cl, c);  
        drawAll3(cl, c);  
        
        
    }
}


