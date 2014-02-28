public class Triple<S extends Comparable<? super S>, 
                    T extends Comparable<? super T>,
                    U extends Comparable<? super U>> 
    implements Comparable<Triple<S,T,U>>
{
    S mFirst;
    T mSecond;
    U mThird;
    
    public Triple(S first, T second, U third) {
        mFirst  = first;
        mSecond = second;
        mThird  = third;
    }

    public int compareTo(Triple<S, T, U> rhs) {
        int cmpFirst = mFirst.compareTo(rhs.getFirst());
        if (cmpFirst != 0) {
            return cmpFirst;
        }
        int cmpSecond = mSecond.compareTo(rhs.getSecond());
        if (cmpSecond != 0) {
            return cmpSecond;
        }
        return mThird.compareTo(rhs.getThird());
    }

    public String toString() {
        return "<" + mFirst + ", " + mSecond + ", " + mThird + ">";
    }

    public S getFirst()  { return mFirst;  }
    public T getSecond() { return mSecond; }
    public U getThird()  { return mThird;  }

    public void setFirst(S first) { 
        mFirst = first; 
    }
    public void setSecond(T second) { 
        mSecond = second; 
    }
    public void setThird(U third) { 
        mThird = third; 
    }
}
