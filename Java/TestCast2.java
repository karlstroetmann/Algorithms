import java.util.*;

public class TestCast2 
{
	public static void main(String[] args) {
		List<Integer> l = new LinkedList<Integer>();
		for (Integer i = 0; i < 10; ++i) {
			l.add(i);
		}
		Integer[] b = l.toArray(new Integer[0]);
		for (int i = 0; i < 10; ++i) {
			System.out.println(b[i]);
		}
	}
}
