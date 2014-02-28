import java.util.*;

public class TestCast 
{
    public static void main(String[] args) {
        List<Integer> l = new LinkedList<Integer>();
        for (Integer i = 0; i < 10; ++i) {
            l.add(i);
        }
        Object [] a = l.toArray();
        Integer[] b = (Integer[]) a;
        System.out.println(b);
    }
}
