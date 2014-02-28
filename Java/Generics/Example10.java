import java.util.*; 

public class Example10 { 
    public static void main(String[] args) { 
		List<String>[] lsa = new List<?>[10]; // compile time error
		Object         o   = lsa;
		Object[]       oa  = (Object[]) o;
		List<Integer>  li  = new ArrayList<Integer>();
		li.add(new Integer(3));
		oa[1] = li;               
		String s = lsa[1].get(0); 
    }
}