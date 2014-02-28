import java.util.*; 

public class CreateArray<T> 
{
	T[] mDummyArray;
	
	public CreateArray(T[] dummyArray) {
		mDummyArray = dummyArray;
	}

	public T[] createArray(int size) {
		ArrayList<T> arrayList = new ArrayList<T>(size);
		for (int i = 0; i < size; ++i) {
			arrayList.add(null);
		}
		return arrayList.toArray(mDummyArray);
	}
}

	