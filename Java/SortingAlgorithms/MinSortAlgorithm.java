import java.util.*;

public class MinSortAlgorithm extends SortingAlgorithm
{
    MinSortAlgorithm(Double[] array, Comparator<Double> comparator) {
        super(array, comparator);
    }
    
    // This method implements the MinSort algorithm to sort mArray
    public void sort() {
        sort(0);        
    }

    // Sort the array [ mArray[i], ... mArray[ mArray.length - 1] ]
    private void sort(int i) {
        if (i == mArray.length)
            return;
        int minIndex = minIndex(i);
        swap(i, minIndex);
        sort(i + 1);
    }

    // compute the index of a smallest element of the array 
    // [ mArray[first], ... mArray[ mArray.length - 1] ]
    private int minIndex(int first) {
        int minIndex = first;
        for (int i = first + 1; i < mArray.length; ++i) {
            if (mComparator.compare(mArray[minIndex], mArray[i]) > 0) {
                minIndex = i;
            }
        }
        return minIndex;
    }
}

