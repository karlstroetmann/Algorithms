import java.util.*;

public class SetOfSet {
    public static void main(String[] args) {
        TreeSet<TreeSet<Integer>> all = new TreeSet<TreeSet<Integer>>();
        TreeSet<Integer> a = new TreeSet<Integer>();
        a.add(1);
        a.add(2);
        a.add(3);
        all.add(a);
        TreeSet<Integer> b = new TreeSet<Integer>();
        b.add(1);
        b.add(2);
        b.add(3);
        all.add(b);
        System.out.println(all);
    }
}