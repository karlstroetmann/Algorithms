import java.util.*;

public class InsertionSortAlgorithm extends SortingAlgorithm
{
    InsertionSortAlgorithm(Double[] array, Comparator<Double> comparator) {
        super(array, comparator);
        mAux = new Double[mArray.length];
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
        moveToAux(i, i, true);
        int j = i + 1;
        while (j < mArray.length) {
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
        if (j == mArray.length) {
            stop();
            mArray[j-1] = mAux[i];
            mAux[i]     = null;
        }
    }
}

