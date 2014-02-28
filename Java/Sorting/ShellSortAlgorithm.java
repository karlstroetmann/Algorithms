import java.util.*;

public class ShellSortAlgorithm extends SortingAlgorithm
{
    ShellSortAlgorithm(Double[] array, Comparator<Double> comparator) {
        super(array, comparator);
        mAux = new Double[mArray.length];
    }

    /** This method implements the Shell Sort Algorithm algorithm 
     *  to sort mArray.
     */
    public void sort() {
        for (int l = maxThree(mArray.length); l >= 1; l = (l - 1) / 3) {
            partialSort(l);
        }
    }

    /** If we define the function
     *     threeTimes : N -> N
     *  recursively as
     *     threeTimes(0)   = 1
     *     threeTimes(l+1) = 3 * threeTimes(l) + 1
     *  then the function maxThree is defined as follows:
     *  maxThree(n) = max {threeTimes(l): l in {1..n} | threeTimes(l) <= n/9}.
     */
    private int maxThree(int n) {
        int a   = 1;
        int max = a;
        while (a <= n / 9) {
            max = a;
            a = 3 * a + 1;
        }
        return max;
    }

    /** This method sorts the following l subarrays independently:
     *    [ mArray[0 * l + 0], mArray[1 * l + 0], mArray[2 * l + 0], ... ],
     *    [ mArray[0 * l + 1], mArray[1 * l + 1], mArray[2 * l + 1], ... ],
     *    [ mArray[0 * l + 2], mArray[1 * l + 2], mArray[2 * l + 2], ... ],
     *    ...
     *    [ mArray[0 * l + l-1], mArray[1 * l + l-1], mArray[2 * l + l-1], ... ],
     */
    private void partialSort(int l) {
        for (int k = 0; k < l; ++k) {
            for (int j = k; j < mArray.length; j = j + l) {
                moveToAux(j, j, true);
            }
            partialSort(k, l);
            for (int j = k; j < mArray.length; j = j + l) {
                moveToArray(j);
            }
        }
    }

    /** This method sorts the subarray
     *    [ mArray[0 * l + k], mArray[1 * l + k], mArray[2 * l + k], ... ].
     */
    private void partialSort(int k, int l) {
        if (k >= mArray.length) {
            return;
        }
        int r = mArray.length - k;
        int lastIndex = k + ((r % l == 0) ? (r / l - 1) * l : (r / l) * l);
        int i = lastIndex;
        for (; i >= 0; i = i - l) {
            insert(i, l);
        }
        assert i + l == k : 
               "wrong index: i = " + i + ", k = " + k + ", l = " + l;
    }

    /** This method inserts the value mAux[i] into the subarray
     *    [ mArray[l + i], mArray[2 * l + i], mArray[3 * l + i], ... ]
     *  so that the resulting subarray ist sorted.
     */
    private void insert(int i, int l) {
        moveToArray(i);
        int j = i + l;
        while (j < mArray.length) {
            if (mComparator.compare(mArray[i], mAux[j]) > 0) {
                mAux[j-l]   = mAux[j];
                mAux[j]     = null;
                mArray[i+l] = mArray[i];
                mArray[i]   = null;
                j += l; 
                i += l;
            } else {
                mAux[j-l] = mArray[i];
                mArray[i] = null;
                break;
            }
        }
        if (j >= mAux.length) {
            stop();
            mAux[j-l] = mArray[i];
            mArray[i] = null;
        }
    }
}

