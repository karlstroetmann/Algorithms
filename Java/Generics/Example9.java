import java.util.*; 

public class Example9 { 
    public static void main(String[] args) { 
	List<?>[]     lsa = new List<?>[10]; // ok now
	Object        o   = lsa;
	Object[]      oa  = (Object[]) o;
	List<Integer> li  = new ArrayList<Integer>();
	li.add(new Integer(3));
	oa[1] = li;               // ok
	String s = (String) lsa[1].get(0); // run-time error now
    }
}