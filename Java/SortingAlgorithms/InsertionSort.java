import java.util.*;
import static java.lang.Math.*;

public class InsertionSort {
    LinkedList<Double> mList;
    
    InsertionSort(Double[] a) {
        mList = new LinkedList<Double>();
        for (Double x : a) {
            mList.add(x);
        }
    }

    public void sort() {
        if (mList.isEmpty()) {
            return;
        }
        Double x = mList.removeFirst();
        sort();
        insert(x);
    }

    private void insert(Double x) {
        if (mList.isEmpty()) {
            mList.addFirst(x);
            return;
        }
        Double y = mList.getFirst();
        if (x <= y) {
            mList.addFirst(x);
        } else {
            mList.removeFirst();  // remove y
            insert(x);
            mList.addFirst(y);
        }
    }
    
    public static void main(String[] args) {
        int n = 10;
        Random r = new Random(0);
        Double[] a = new Double[n];
        for (int i = 0; i < n; ++i) {
            a[i] = r.nextDouble();
        }
        InsertionSort IS = new InsertionSort(a);
        System.out.println(IS.mList);
        IS.sort();
        System.out.println(IS.mList);
    }
}