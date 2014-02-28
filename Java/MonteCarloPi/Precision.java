import java.util.*;

public class Precision {

    public static void main(String[] args) {
        Double p = 0.25 * Math.PI;        
        for (long n = 10; n < 1000000000000L; n = n * 10) {
            Double epsilon = 3 * Math.sqrt(p * (1 - p) / n);
            System.out.printf("n = %15d, epsilon = %10.8f\n", n, epsilon);
        }
    }
}