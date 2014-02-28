import java.util.*;

public class FibonacciDynamic
{
	public static void main(String[] args) 
	{
		for (int i = 0; i < 100; ++i) {
			int n = fibonacci(i);
			System.out.printf("fib(%d) = %d\n", i, n);
		}
	}
	
	public static int fibonacci(int n) 
	{
		if (n <= 2) {
			return 1;
		}
		int[] mem = new int[n+1];
		mem[0] = 1;  // fibonacci(0) = 1
		mem[1] = 1;  // fibonacci(1) = 1
		for (int i = 0; i < n - 1; ++i) {
			mem[i + 2] = mem[i] + mem[i + 1];
		}
		return mem[n];
	}
}
