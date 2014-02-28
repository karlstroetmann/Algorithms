import java.util.*;

public class QuickSort
{  

    private static void partition(Double pivot, LinkedList<Double> list,
                                  LinkedList<Double> small, LinkedList<Double> big) 
    {
        if (list.isEmpty()) {
            return;
        }
        Double x = list.removeFirst();
        if (x <= pivot) {
            small.addFirst(x);
        } else {
            big.addFirst(x);
        }
        partition(pivot, list, small, big);
    }
    public static LinkedList<Double> sort(LinkedList<Double> list) {
        if (list.isEmpty()) {
            return list;
        }
        Double pivot = list.removeFirst();
        LinkedList<Double> small = new LinkedList<Double>();
        LinkedList<Double> big   = new LinkedList<Double>();
        partition(pivot, list, small, big);
        LinkedList<Double> smallSorted = sort(small);
        LinkedList<Double> bigSorted   = sort(big);
        smallSorted.add(pivot);
        smallSorted.addAll(bigSorted);
        return smallSorted;
    }

    public static void main(String[] args) {
        Double[] a = { 3.0, 7.0, 5.0, 2.0, 4.0, 2.0, 11.0, 1.0 };
        LinkedList<Double> list = new LinkedList<Double>();
        for (Double x : a) {
            list.add(x);
        }
        System.out.println(list);
        list = sort(list);
        System.out.println(list);
    }
}

