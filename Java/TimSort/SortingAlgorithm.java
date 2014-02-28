import java.util.*;

public abstract class SortingAlgorithm 
{
    protected Double[] mArray;      // the array to be sorted
    protected Double[] mAux;        // an auxilliary array 
    protected Double[] mOriginal;   // the original unsorted array

    SortingAlgorithm(Double[] array) {
        mArray      = array;
        mAux        = null;
        mOriginal   = null;
    }

    public abstract void sort();

    public void testSort() {
        assert saveOriginal();
	System.out.println(myString());
        sort();
	System.out.println(myString());
        assert isSorted()     : "*** Error: mArray is not sorted! ***";
        assert sameElements() : "*** Error: mArray has different elements than the original array! ***";
    }

    public Double[] getAux() {
        return mAux;
    }

    public boolean saveOriginal() {
        mOriginal = new Double[mArray.length];
        for (int i = 0; i < mArray.length; ++i) {
            mOriginal[i] = mArray[i];
        }
        return true;
    }    
    
    public boolean isSorted() {
        for (int i = 0; i + 1 < mArray.length; ++i) {
            if (mArray[i] > mArray[i+1]) {
                return false;
            }
        }
        return true;
    }

    public boolean isSorted(int low, int high) {
        for (int i = low; i + 1 < high; ++i) {
            if (mArray[i] > mArray[i+1]) {
                return false;
            }
        }
        return true;
    }

    public boolean sameElements() {
        for (Double x: mArray) {
            if (count(mArray, x) != count(mOriginal, x)) {
                return false;
            }
        }
        return true;
    }
    
    protected static int count(Double[] array, Double value) {
        int result = 0;
        for (Double x: array) {
            if (value.equals(x)) {
                ++result;
            }
        }
        return result;
    }        

    protected void moveToAux(int i, int j, boolean stop) {
        mAux[j]   = mArray[i];
        mArray[i] = null;
    }

    protected void moveToArray(int i) {
        mArray[i] = mAux[i];
        mAux[i]   = null;
    }

    // swap the elements at position i and j in mArray
    protected void swap(int i, int j) 
    {
        if (i == j) return;
        Double temp = mArray[i];
        mArray[i]   = mArray[j];
        mArray[j]   = temp;
    }

    // swap the elements at position i and j in mAux
    protected void swapAux(int i, int j) 
    {
        if (i == j) return;
        Double temp = mAux[i];
        mAux[i]     = mAux[j];
        mAux[j]     = temp;
    }

    public String myString() {
	String result = "";
	for (Double x: mArray) {
	    result += x + " ";
	}
	return result;
    }

    public String auxString() {
	String result = "";
	for (Double x: mAux) {
	    result += x + " ";
	}
	return result;
    }
}

