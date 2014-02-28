import java.util.*;
import java.math.*;


public class FastSqrt 
{
    static BigInteger square;

    public static void main(String[] args) 
    {
        BigInteger two = BigInteger.valueOf(2);
        for (int i = 0; i < 1000; ++i) {
            two = two.multiply(BigInteger.valueOf(100));
        }
		System.out.println("Starting to calculate root ...");
        System.out.println(intSqrt(two));
    }

    public static BigInteger intSqrt(BigInteger n) 
    {
        BigInteger zero = BigInteger.valueOf(0);
        if (n.equals(zero)) {
            square = zero;
            return zero;
        } else {
            BigInteger one  = BigInteger.valueOf(1);
            BigInteger two  = BigInteger.valueOf(2);
            BigInteger four = BigInteger.valueOf(4);
            BigInteger x    = intSqrt(n.shiftRight(2));
            BigInteger y    = x.shiftLeft(1);
            BigInteger z    = y.add(one);
            BigInteger a    = square.shiftLeft(2);
            BigInteger b    = x.shiftLeft(2);
            BigInteger c    = a.add(b).add(one);
            if (c.compareTo(n) <= 0) {
                square = c;
                return z;
            } else {
                square = a;
                return y;
            }
        }
    }   
}
