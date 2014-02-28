public class Pair<S extends Comparable<S>, T> 
{
	S mFirst;
	T mSecond;
	
	public Pair(S first, T second) {
		mFirst  = first;
		mSecond = second;
	}

	public String toString() {
		return "<" + mFirst + ", " + mSecond + ">";
	}

	public S getFirst()  { return mFirst;  }
	public T getSecond() { return mSecond; }

	public void setFirst(S first) { 
		mFirst = first; 
	}
	public void setSecond(T second) { 
		mSecond = second; 
	}
}
