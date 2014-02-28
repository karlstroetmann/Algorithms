import java.util.*;

public abstract class SortingAlgorithm 
{
	protected Double[]           mArray;      // the array to be sorted
	protected Double[]           mAux;        // an auxilliary array 
    protected Double[]           mOriginal;   // the original unsorted array
	protected Comparator<Double> mComparator; // an object to compare two values 

	SortingAlgorithm(Double[] array, Comparator<Double> comparator) {
		mArray      = array;
		mAux        = null;
		mOriginal   = null;
		mComparator = comparator;
	}

	public abstract void sort();

	public void testSort() {
		assert saveOriginal();
		sort();
		assert isSorted() : 
			"*** Error: mArray is not sorted! ***\n" + ts(mOriginal) + "\n" + ts(mArray);
		assert sameElements() : 
			"*** Error: mArray has different elements than the original array! ***";
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
			if (mComparator.compare(mArray[i], mArray[i+1]) > 0) {
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
		if (stop) {
			stop();
		}
		mAux[j]   = mArray[i];
		mArray[i] = null;
	}

	protected void moveToArray(int i) {
		stop();
		mArray[i] = mAux[i];
		mAux[i]   = null;
	}

	// fake call of compare to stop the animation
	protected void stop() {
		mComparator.compare(0.0, 1.0);
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

	private String ts(Double[] array) {
		String s = "";
		for (int i = 0; i < array.length; ++i) {
			s += array[i] + " ";
		}
		return s;
	}
}
