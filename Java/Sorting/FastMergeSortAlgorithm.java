import java.util.*; 

import static java.lang.Math.*;

public class FastMergeSortAlgorithm extends SortingAlgorithm {
    
    public static long time2 = System.currentTimeMillis();
    public static long time1 = System.currentTimeMillis();
    
     /** Creates a new instance of MyMergeSort */
    FastMergeSortAlgorithm(Double[ ] array, Comparator<Double> comparator) {
        super(array, comparator);
        mAux = new Double[mArray.length];
    }

    public Double[] getAux() {
        return mAux;
    }
    
    /* Sortierfunktion, Felder der Laenge<2 sind sortiert*/
    public void sort() {
        if (mArray.length>2) {
            mergeSort();
        }
    }

    // First, small parts are sorted by insertion sort.
    // Then, the array is sorted bottom up by merge sort.
    public void mergeSort() {        
        int length    = mArray.length;
        int smallPart = 5;  // parts of this length are sorted by insertionSort
        for (int i = 0; i < length; i += smallPart) {
            insertionSort(i, min(length, i + smallPart));
        }
        // Next, we switch to merge sort. 
        for (int l = smallPart; l < length; l *= 2) {
            int k;
            for (k = 0; l * (k + 1) <= length; k += 2) {
                merge(l * k, l * (k + 1), min(length, l * (k + 2)));
            }
        }        
    }
    
    private void merge(int start, int middle, int end) {    
                // copy everything to the auxilliary array mAux
        for (int i = start; i < end; ++i) {
            stop();
            mAux[i]   = mArray[i]; 
            mArray[i] = null;  // only usefull for animation
        }
        // The number idx1 indexes the first part of the array.
        int idx1 = start;
        // The number idx2 indexes the second part of the array.
        int idx2 = middle;
        // The number i indexes the resulting merged part.
        int i = start;
        while (idx1 < middle && idx2 < end) {
            if (mComparator.compare(mAux[idx1], mAux[idx2]) <= 0) {
                mArray[i]  = mAux[idx1]; 
                mAux[idx1] = null;
                ++i;
                ++idx1;
            } else {
                mArray[i]  = mAux[idx2]; 
                mAux[idx2] = null;
                ++i;
                ++idx2;
            }
        }
        // If any elements are left in the first part, we need to copy them.
        while (idx1 < middle) {
            stop();
            mArray[i]  = mAux[idx1];
            mAux[idx1] = null;
            ++i;
            ++idx1;
        }
        // If any elements are left in the second part, we copy them.
        while (idx2 < end) {
            stop();
            mArray[i]  = mAux[idx2];  
            mAux[idx2] = null;
            ++i;
            ++idx2;
        }
    }
    
    /** The method sorts the following part of mArray:
     *       [ mArray[lower], mArray[lower+1], ..., mArray[upper-1] ].
     */
    public void insertionSort(int lower, int upper) {
        for (int i = upper - 1; i >= lower; --i) {
            insert(i, upper);
        }
    }

    /** The method assumes, that the array
     *       [ mArray[i+1], mArray[i+2], ..., mArray[upper-1] ]
     *  is sorted.  This method inserts mArray[i] into the array above so 
     *  that afterwards the array
     *       [ mArray[i], mArray[i+1], mArray[i+2], ..., mArray[upper-1] ]
     *  is sorted.
     */
    private void insert(int i, int upper) {
        moveToAux(i, i, true);
        int j = i + 1;
        while (j < upper) {
            if (mComparator.compare(mAux[i], mArray[j]) > 0) {
                mArray[j-1] = mArray[j];
                mArray[j]   = null;
                mAux[i+1]   = mAux[i];
                mAux[i]     = null;
                ++j; ++i;
            } else {
                mArray[j-1] = mAux[i];
                mAux[i]     = null;
                break;
            }
        }
        if (j == upper) {
            stop();
            mArray[j-1] = mAux[i];
            mAux[i]     = null;
        }
    }

}
