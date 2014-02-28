import java.util.*;

public class BubbleSortAlgorithm extends SortingAlgorithm
{
	BubbleSortAlgorithm(Double[] array, Comparator<Double> comparator) {
		super(array, comparator);
	}
	
	/** This method implements the BubbleSort algorithm to sort mArray */
	public void sort() {
		int n = mArray.length;
		for (int i = 0; i < n; ++i) {
			// invariant: mArray[(n-1) - i .. n-1] is sorted
			//            all elements of mArray[(n-1) - i .. n-1] 
			//            are bigger than any element of mArray[0 .. (n-1) - i]
			for (int j = 0; j < n - 1 - i; ++j) {
				if (mComparator.compare(mArray[j], mArray[j+1]) > 0) {
					swap(j, j + 1);
				}
			}
		}
	}
}

