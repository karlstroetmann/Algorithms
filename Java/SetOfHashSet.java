import java.util.*;

public class SetOfHashSet {
    public static void main(String[] args) {
        HashSet<HashSet<Integer>> all = new HashSet<HashSet<Integer>>();
        HashSet<Integer> a = new HashSet<Integer>();
        a.add(1);
        a.add(2);
        a.add(3);
        all.add(a);
        HashSet<Integer> b = new HashSet<Integer>();
        b.add(1);
        b.add(2);
        b.add(3);
        all.add(b);
        System.out.println(all);
    }
}