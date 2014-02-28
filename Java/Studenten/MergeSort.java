import java.util.Comparator;

public class MergeSort extends SortingAlgorithm
{

	MergeSort(Double[] array, Comparator<Double> comparator) {
		super(array, comparator);
	}
	

	public void sort() {
		
		int partArray = 0;
		int first;
		int second;
		int help;
		Double[] copy = new Double[mArray.length];
		if (mArray.length >= 2) 
			partArray = 2;
		do {
			System.arraycopy(mArray, 0, copy, 0, mArray.length);
			for (int i = 0; i + partArray / 2 < mArray.length; i += partArray) {
				first  = i;
				second = i + partArray / 2;
				help   = first;
			 	do {	
					if (copy[first] < copy[second]) {
			 			mArray[help] = copy[first];
			 			first++;
			 			help++;
			 		} else {
			 			mArray[help] = copy[second];
			 			second++;
			 			help++;
			 			
			 		} 
			 	} while (second <  mArray.length    &&
                         second != i + partArray    &&
                         first  != i + partArray / 2  );
				if (second == mArray.length || second == i + partArray) {
					for (int x = help; x < i + partArray && x < mArray.length; x++) {
						mArray[x] = copy[first];
						first++;
					}
				} else if (first == i + partArray/2) {
					for (int x = help; x < i + partArray && x < mArray.length; x++) {
						mArray[x] = copy[second];
						second++;
					}
				}
			}
			partArray *= 2;
		} while (partArray / 2 < mArray.length); 
		// Teilfeld ist dann doppelt so gro§ wie ursprŸngliches Feld, darf nicht sein!
	}
}
