import java.util.*;

public class MergeSort 
{  
    LinkedList<Double> mList;
    
    public MergeSort(Double[] a) 
    {    
        mList = new LinkedList<Double>();
        for (Double x : a) {
            mList.add(x);
        }
    }
    
    public void sort() 
    {
        sort(mList);
    }

    public LinkedList<Double> sort(LinkedList<Double> list) {
        if (list.size() < 2) {
            return list;
        }
        LinkedList<Double> first  = new LinkedList<Double>();
        LinkedList<Double> second = new LinkedList<Double>();
        split(list, first, second);
        LinkedList<Double> firstSorted  = sort(first);
        LinkedList<Double> secondSorted = sort(second);
        return merge(firstSorted, secondSorted);
    }

    public void split(LinkedList<Double> list, 
                      LinkedList<Double> first, LinkedList<Double> second) 
    {
        if (list.size() == 0) {
            return;
        }
        Double x = list.removeFirst();
        if (list.size() == 0) {
            first.addFirst(x);
            return;
        }
        Double y = list.removeFirst();
        split(list, first, second);
        first .addFirst(x);
        second.addFirst(y);
    }

    public LinkedList<Double> merge(LinkedList<Double> first, LinkedList<Double> second) 
    {
        if (first.size() == 0) {
            return second;
        }
        if (second.size() == 0) {
            return first;
        }
        LinkedList<Double> result;
        Double x = first .getFirst();
        Double y = second.getFirst();
        if (x < y) {
            first.removeFirst();
            result = merge(first, second);
            result.addFirst(x);
        } else {
            second.removeFirst();
            result = merge(first, second);
            result.addFirst(y);
        }
        return result;
    }

    public static void main(String[] args) {
        int n = 50000;
        Random r = new Random(0);
        Double[] a = new Double[n];
        for (int i = 0; i < n; ++i) {
            a[i] = r.nextDouble();
        }
        // System.out.println(IS.mList);
        MergeSort IS = new MergeSort(a);
        //        System.out.println(IS.mList);
        IS.sort();
    }
}

