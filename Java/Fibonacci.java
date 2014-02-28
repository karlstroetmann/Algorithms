public class Fibonacci 
{
    public static int fibonacci(int n) 
    {
        if (n == 0)
            return 1;
        if (n == 1)
            return 1;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    public static void main(String[] args) 
    {
        for (int i = 0; i < 100; ++i) {
            int n = fibonacci(i);
            System.out.printf("fib(%d) = %d\n", i, n);
        }
    }    
}



