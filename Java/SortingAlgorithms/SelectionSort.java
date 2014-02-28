import java.util.*;

public class SelectionSort {
    LinkedList<Double> mList;
    
    SelectionSort(Double[] a) {
        mList = new LinkedList<Double>();
        for (Double x : a) {
            mList.add(x);
        }
    }

    public void sort() {
        if (mList.isEmpty()) {
            return;
        }
        Double x = min();
        mList.remove(x);
        sort();
        mList.addFirst(x);
    }

    private Double min() {
        Double min = mList.getFirst();
        for (Double x : mList) {
            min = Math.min(min, x);
        }
        return min;
    }
    
    public static void main(String[] args) {
        Double[] a = { 3.0, 7.0, 5.0, 2.0, 4.0, 11.0, 1.0, 7.0, 3.0 };
        InsertionSort IS = new InsertionSort(a);
        System.out.println(IS.mList);
        IS.sort();
        System.out.println(IS.mList);
    }
}