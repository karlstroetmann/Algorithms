import java.util.*; 

public class MergeSortAlgorithm extends SortingAlgorithm
{
    MergeSortAlgorithm(Double[] array, Comparator<Double> comparator) {
        super(array, comparator);
        mAux = new Double[mArray.length];
    }

    // This method implements the merge sort algorithm algorithm to sort mArray
    public void sort() {
        mergeSort(0, mArray.length);
    }

    // This method sorts part of mArray.  The region that needs to be sorted is
    // specified via start and end:
    //     mArray[start] ... mArray[end-1]
    // is the region of mArray that is sorted.  
    private void mergeSort(int start, int end) {
        // If there are less than two elements, there is nothing to do.
        if (end - start < 2)
            return;
        // Otherwise, we split the portion to be sorted into two parts of
        // approximately the same size, sort these parts recursively, and merge
        // the sorted parts.
        int middle = (start + end) / 2;                         
        mergeSort( start,  middle );  
        mergeSort( middle, end    );    
        merge( start, middle, end ); 
    }

    // This method merges two parts of mArray.  The two parts are
    //    mArray[start]  ... mArray[middle - 1]  and
    //    mArray[middle] ... mArray[end-1]
    // respectively.  Both of these parts are assumed to be sorted.  On return
    // of the method, the elements in the range
    //        mArray[start] ... mArray[end-1] 
    // are sorted.
    private void merge(int start, int middle, int end) {    
        // copy everything to the auxilliary array mAux
        for (int i = start; i < end; ++i) {
            stop();                // animation
            mAux[i]   = mArray[i]; 
            mArray[i] = null;      // only usefull for animation
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
                mAux[idx1] = null;        // animation
                ++i;
                ++idx1;
            } else {
                mArray[i]  = mAux[idx2]; 
                mAux[idx2] = null;        // animation
                ++i;
                ++idx2;
            }
        }
        // If any elements are left in the first part, we need to copy them.
        while (idx1 < middle) {
            stop();
            mArray[i]  = mAux[idx1];
            mAux[idx1] = null;            // animation
            ++i;
            ++idx1;
        }
        // If any elements are left in the second part, we copy them.
        while (idx2 < end) {
            stop();
            mArray[i]  = mAux[idx2];  
            mAux[idx2] = null;            // animation
            ++i;
            ++idx2;
        }
    }
}
