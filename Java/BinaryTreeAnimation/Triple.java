// This class represent the triple <mFirst, mSecond, mThird>
public class Triple<First, Second, Third>
{
    First  mFirst;
    Second mSecond;
    Third  mThird;
    
    Triple(First first, Second second, Third value) {
        mFirst  = first;
        mSecond = second;
        mThird  = value;
    }

    First  getFirst()  { return mFirst;  }
    Second getSecond() { return mSecond; }
    Third  getThird()  { return mThird;  }
}
