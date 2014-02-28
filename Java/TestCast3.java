import java.util.*;

public class TestCast3 
{
	public static void main(String[] args) {
		List<Integer> l = new LinkedList<Integer>();
		for (Integer i = 0; i < 10; ++i) {
			l.add(i);
		}
		String[] a = new String[0];
		String[] b = l.toArray(a);
		System.out.println(b);
	}
}
