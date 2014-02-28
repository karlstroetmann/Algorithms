import java.util.*;
import java.math.*;


public class PowerIterative 
{
    public static void main(String[] args)
    {
        for (int i = 0; i <= 32; ++i) {
            BigInteger base = BigInteger.valueOf(137);
            System.out.println("power(" + 3 + ", " + i + ") = " + power(base, i));
        }
    }
    
    static BigInteger power(BigInteger m, int n)
    {
        BigInteger r = BigInteger.valueOf(1);
        for (int i = 0; i < n; ++i) {
            r = r.multiply(m);
        } 
        return r;
    }
}
