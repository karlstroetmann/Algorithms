import java.util.*;

public class CalculatePi {
    private Random mRandom;
    
    public CalculatePi() {
        mRandom = new Random();
    }

    /**
     * Calculate Pi using the Monte-Carlo method
     * @param   number of trials
     * @return  an approximation of pi
     */
    public double calcPi(long n) {
        long k = 0;  // k is the number of points that are inside the circle
        for (long i = 0; i < n; ++i) {
            double x = 2 * mRandom.nextDouble() - 1;
            double y = 2 * mRandom.nextDouble() - 1;
            double r = x * x + y * y;
            if (r <= 1) {
                ++k;
            }
        }
        return 4.0 * k / n;
    }

    public static void main(String[] args) {
        CalculatePi c = new CalculatePi();
        for (long n = 10; n < 1000000000000L; n = n * 10) {
            double pi = c.calcPi(n);
            System.out.printf("n = %14d: %g,  Fehler: %+f\n", n, pi, pi - Math.PI);
        }
    }
}