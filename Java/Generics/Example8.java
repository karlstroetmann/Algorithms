import java.util.*; 

public class Example8 { 
    public static void main(String[] args) { 
	List<String>[] lsa = new List<String>[10]; // not really allowed
	Object         o   = lsa;
	Object[]       oa  = (Object[]) o;
	List<Integer>  li  = new ArrayList<Integer>();
	li.add(new Integer(3));
	oa[1] = li;    // unsound, but passes run time store check
	String s = lsa[1].get(0); // run-time error - ClassCastException
    }
}