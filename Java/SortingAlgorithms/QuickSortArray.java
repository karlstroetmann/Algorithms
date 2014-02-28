import java.util.*;

public class QuickSortArray 
{
    private Double[] mArray;  // the array to be sorted

    QuickSortArray(Double[] array) {
        mArray = array;
    }
    
    /** This method implements the Insertion Sort Algorithm algorithm 
     *  to sort mArray.
     */
    public void sort() {
        quickSort(0, mArray.length - 1);
    }

    /** This function sorts part of the array mArray.  The part that is 
        sorted is given as
           { mArray[start], ..., mArray[end] }.
     * @param start index to the first position of the part to be sorted
     * @param end   index to the last  position of the part to be sorted
     */
    private void quickSort(int start, int end) {
        // If the part contains just one element, then there is nothing to do.
        if (end <= start) {
            return;
        }
        int splitIdx = partition(start, end);
        quickSort(start, splitIdx - 1);  
        quickSort(splitIdx + 1, end);    
    }
    /** This function defines x := array[start] and partitions the array given as
     *  first argument into two parts.  The first part contains all elements that
     *  are less or equal than x, the second part contains the remaining elements.
     *  The arguments start and end specify the part of the array that is to be
     *  partitioned.  The partitioning is done in place.  The function returns the
     *  index i which the element x takes in the new array.  Formally, when the
     *  function terminates with result i, the following is satisfied:
     *
     *    1.  forall j in {start..i-1} : array[j] <= x
     *    2.  forall j in {i+1..end}   : x < array[j]
     *    3.  array[i] = x.
     */
    int partition(int start, int end) {
        Double x     = mArray[start];
        int leftIdx  = start + 1;
        int rightIdx = end;
        while (true) {
            // search for an element bigger than x
            while (leftIdx <= end && mArray[leftIdx] <= x)
            {
                ++leftIdx;
            }
            //! leftIdx <= end -> mArray[leftIdx] > x
            // search for an element less or equal than x
            while (mArray[rightIdx] > x) 
            {
                --rightIdx;
            }
            //! rightIdx > start -> mArray[rightIdx] <= x
            if (leftIdx >= rightIdx) {
                assert leftIdx == rightIdx + 1 : "left == right";
                break;
            }
            //! leftIdx < rightIdx
            //! leftIdx <= end
            //! start   <  rightIdx
            swap(leftIdx, rightIdx);
        }
        //! rightIdx < leftIdx & mArray[rightIdx] <= x
        swap(start, rightIdx);
        return rightIdx;    
    }

    // swap the elements at position i and j in mArray
    protected void swap(int i, int j) 
    {
        if (i == j) return;
        Double temp = mArray[i];
        mArray[i]   = mArray[j];
        mArray[j]   = temp;
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
        Double[] a = { 3.0, 7.0, 5.0, 2.0, 4.0, 11.0, 1.0, 7.0, 3.0 };
        QuickSortArray ms = new QuickSortArray(a);
        ms.print();
        ms.sort();
        ms.print();
    }
}

