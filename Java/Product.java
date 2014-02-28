import java.util.*;
import java.math.*;

public class Product
{
    public static void main(String[] args)
    {
        for (int i = 1; i <= 10; ++i) {
            for (int j = 1; j <= 10; ++j) {
                System.out.printf("%3d ", multiply(i, j));
            }
            System.out.println("");
        }
    }
    
    static int multiply(int m, int n)
    {
        if (n == 0)
            return 0;
        int p = multiply(m, n >> 1);
        if (n % 2 == 0) {
            return p << 1;
        } else {
            return (p << 1) + m;
        }
    }
}
