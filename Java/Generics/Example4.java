import java.util.*;

public class Example4 {

    public static void main(String[] args) {
        List<Integer> intList = new LinkedList<Integer>();
        intList.add(2);
        intList.add(4);
        intList.add(6);
		//  printList1(intList);  // won't work
		printList2(intList);  
		printList3(intList);  
    }

    // only usable for lists of objects, not usable for lists of Integers
    static void printList1(List<Object> l) {
		for (Object x: l) {
			System.out.println(x);
		}
    }

    // using a wildcard works
    static void printList2(List<?> l) {
		for (Object x: l) {
			System.out.println(x);
		}
    }

    // using a generic method
    static <T> void printList3(List<T> l) {
		for (T x: l) {
			System.out.println(x);
		}
    }

}