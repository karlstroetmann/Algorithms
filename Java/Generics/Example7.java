import java.util.*; 

public class Example7 {
    public static void main(String[] args) {
		List<String>  l1 = new LinkedList<String>();
		List<Integer> l2 = new LinkedList<Integer>();
		System.out.println(l1.getClass() == l2.getClass());
    }
}