import java.util.*;

public class Example2 {

    public static void main(String[] args) {
        List<Integer> intList = new LinkedList<Integer>();
        intList.add(2);
        intList.add("four");  // type error now
        intList.add(6);
        for (Integer x: intList) {
            System.out.println( x / 2 );
        }
    }
}