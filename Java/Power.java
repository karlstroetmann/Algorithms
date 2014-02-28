import java.util.*;
import java.math.*;


public class Power 
{
    public static void main(String[] args)
    {
        for (int i = 0; i <= 32; ++i) {
            BigInteger base = BigInteger.valueOf(3);
            System.out.println("power(" + 3 + ", " + i + ") = " + power(base, i));
        }
    }
    
    static BigInteger power(BigInteger m, int n)
    {
        if (n == 0)
            return BigInteger.valueOf(1);
        BigInteger p = power(m, n / 2);
        if (n % 2 == 0) {
            return p.multiply(p);
        } else {
            return p.multiply(p).multiply(m);
        }
    }
}
