import java.util.*; 
import static java.lang.Math.*;

// This class gives a non-recursive implementation of the merge-sort algorithm.
public class MergeSortNR 
{
    private Double[] mArray;  // the array to sort
    private Double[] mAux;    // an auxilliary array holding a copy of mArray

    MergeSortNR(Double[] array) {
        mArray = array;
        mAux   = new Double[mArray.length];
    }

    // This method implements the merge sort algorithm to sort mArray
    private void sort() {
        // l is the length of the parts to be merged
        for (int l = 1; l < mArray.length; l *= 2) {
            for (int k = 0; l * (k + 1) <= mArray.length; k += 2) {
                merge(l * k, l * (k + 1), min(l * (k + 2), mArray.length));
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
        System.out.println("merge(" + start + ", " + middle + ", " + end + ")");
        // copy everything to the auxilliary array mAux
        for (int i = start; i < end; ++i) {
            mAux[i]   = mArray[i]; 
            mArray[i] = null;  // only useful for animation
        }
        // The number idx1 indexes the first part of the array.
        int idx1 = start;
        // The number idx2 indexes the second part of the array.
        int idx2 = middle;
        // The number i indexes the resulting merged part.
        int i = start;
        while (idx1 < middle && idx2 < end) {
            if (mAux[idx1] <= mAux[idx2]) {
                mArray[i] = mAux[idx1]; 
                ++i;
                ++idx1;
            } else {
                mArray[i] = mAux[idx2]; 
                ++i;
                ++idx2;
            }
        }
        // If any elements are left in the first part, we need to copy them.
        while (idx1 < middle) {
            mArray[i] = mAux[idx1];
            ++i;
            ++idx1;
        }
        // If any elements are left in the second part, we copy them.
        while (idx2 < end) {
            mArray[i] = mAux[idx2];  
            ++i;
            ++idx2;
        }
    }

    private void print() {
	for (int i = 0; i < mArray.length; ++i) {
	    System.out.print(mArray[i]);
	    if (i + 1 < mArray.length) {
		System.out.print(", ");
	    }
	}
	System.out.println("");
    }

    public static void main(String[] args) {
        Double[] a = { 3.0, 7.0, 5.0, 2.0, 4.0, 11.0, 1.0, 7.0, 3.0, 13.0, 15.0, 2.0 };
        MergeSortNR ms = new MergeSortNR(a);
        ms.print();
        ms.sort();
        ms.print();
    }
}
