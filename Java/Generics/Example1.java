import java.util.*;

public class Example1 {

    public static void main(String[] args) {
        List intList = new LinkedList();
        intList.add(2);
        intList.add("four");
        intList.add(6);
        for (Object x: intList) {
            System.out.println( ((Integer) x) / 2 );
        }
    }
}