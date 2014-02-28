import java.util.*;

public class RandomPermutation {
    static Random mRandom = new Random(1);

    public static void permute(int[] array) {
        int n = array.length;
        for (int i = n; i > 1; --i) {
            int  k = mRandom.nextInt(i);
            swap(array, i - 1, k);
        }
    }

    private static void swap(int[] array, int i, int j) {
        int t = array[i];
        array[i] = array[j];
        array[j] = t;
    }
        
    public static void main(String[] args) {
        int   n = 10;
        int[] a = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = i;
        }
        String oldA = "old a: ";
        for (int i = 0; i < n; ++i) {
            oldA += a[i] + " ";
        }
        System.out.println(oldA);
        for (int k = 0; k < 100; ++k) {
            permute(a);
            String newA = "new a: ";
            for (int i = 0; i < n; ++i) {
                newA += a[i] + " ";
            }
            System.out.println(newA);
        }
    }
}