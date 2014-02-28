import java.math.*;

public class FibonacciBig
{
    public static BigInteger fibonacci(int n) 
    {
        if (n < 2) {
            return BigInteger.valueOf(1);
        }
        BigInteger[] mem = new BigInteger[n+1];
        mem[0] = BigInteger.valueOf(1);  // fibonacci(0) = 1
        mem[1] = BigInteger.valueOf(1);  // fibonacci(1) = 1
        for (int i = 0; i < n - 1; ++i) {
            mem[i + 2] = mem[i].add(mem[i + 1]);  // mem[i] + mem[i+1]
        }
        return mem[n];
    }

    public static void main(String[] args) 
    {
        for (int i = 0; i < 100; ++i) {
            BigInteger n = fibonacci(i);
            System.out.println("fib(" + i + ") = " + n);
        }
    }    
}
