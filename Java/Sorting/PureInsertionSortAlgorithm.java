import java.util.*;

public class PureInsertionSortAlgorithm extends SortingAlgorithm
{
    PureInsertionSortAlgorithm(Double[] array, Comparator<Double> comparator) {
        super(array, comparator);
    }
    
    /** This method implements the Insertion Sort Algorithm algorithm 
     *  to sort mArray.
     */
    public void sort() {
        for (int i = mArray.length - 1; i >= 0; --i) {
            insert(i);
        }
    }

    /** The method assumes, that the array
     *              [ mArray[i+1], mArray[i+2], ... ]
     *  is sorted.  This method inserts mArray[i] into the array above so 
     *  that afterwards the array
     *              [ mArray[i], mArray[i+1], ... ]
     *  is sorted.
     */
    private void insert(int i) {
        int j = i + 1;
        Double x = mArray[i];
        while (j < mArray.length) {
            if (mComparator.compare(x, mArray[j]) > 0) {
                mArray[j-1] = mArray[j];
                ++j;
            } else {
                mArray[j-1] = x;
                break;
            }
        }
        if (j == mArray.length) {
            mArray[j-1] = x;
        }
    }
}

