import java.util.*;

public class TestSet 
{
	public static void main(String[] args) 
	{
		TreeSet<Integer> c = new TreeSet<Integer>();
		c.add(1);
		c.add(2);
		TreeSet<Integer> d = new TreeSet<Integer>();
		d.add(3);
		d.add(4);
		TreeSet<Integer> cCopy = new TreeSet<Integer>(c);
		for (Integer n: c) {
			System.out.println(n);
		}
		System.out.println("vereinigen");
		cCopy.addAll(d);
		for (Integer n: c) {
			System.out.println(n);
		}
		System.out.println("cCopy");
		for (Integer n: cCopy) {
			System.out.println(n);
		}
	}
}
