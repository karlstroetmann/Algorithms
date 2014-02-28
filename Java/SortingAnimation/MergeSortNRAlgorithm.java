import java.util.*; 

public class MergeSortNRAlgorithm extends SortingAlgorithm
{
	MergeSortNRAlgorithm(Double[] array, Comparator<Double> comparator) {
		super(array, comparator);
		mAux = new Double[mArray.length];
	}

	// This method implements the merge sort algorithm algorithm to sort mArray
	public void sort() {
		mergeSort();
	}

    // This method sorts mArray via the merge-sort algorithm.  
    private void mergeSort() {
        // l is the length of the parts to be merged
        for (int l = 1; l < mArray.length; l *= 2) {
            int k;
            for (k = 0; l * (k + 1) <= mArray.length; k += 2) {
                merge(l * k, l * (k + 1), Math.min(l * (k + 2), mArray.length));
            }
            // At this point, the array is divided into parts of length
            // 2*l, which are sorted.
        }
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
