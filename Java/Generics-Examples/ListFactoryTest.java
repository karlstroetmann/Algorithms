import java.util.*;
import java.lang.*;

class ListFactory {
    public <A> LinkedList<A> empty() {
	return new LinkedList<A>();
    }
    public <A> LinkedList<A> singleton(A x) {
	LinkedList<A> xs = new LinkedList<A>();
	xs.add(x);
	return xs;
    }
    public <A> LinkedList<A> doubleton(A x, A y) {
	LinkedList<A> xs = new LinkedList<A>();
	xs.add(x);
	xs.add(y);
	return xs;
    }
}

public class ListFactoryTest {
    static ListFactory f = new ListFactory();

    public static void main(String[] args) {
	// not working:
	//	LinkedList<Number> zs = f.doubleton(new Integer(1), new Float(1.0));
	// also not working:
	// Integer i = new Integer(1);
	// Float   f = new Float(1.0);
	// LinkedList<Number> zs = f.doubleton(i, f);
	java.lang.Number i = new Integer(1);
	java.lang.Number f = new Float(1.0);
	LinkedList<java.lang.Number> zs = f.doubleton(i, f);
	LinkedList<String> ys = f.singleton(null);
	LinkedList<Byte>   xs = f.empty();
	LinkedList<Object> err = f.doubleton("abc", new Integer(1));
    }
}
