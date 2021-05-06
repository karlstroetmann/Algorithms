import static java.lang.Math.*;

public class Bisection {
    // This function locates the zero of the function f in the intervall [a, b].
    // It is assumed that a < b and that the function changes the sign in the
    // interval [a, b], i.e. either 
    //        f(a) < 0 and f(b) > 0 or f(a) > 0 and f(b) < 0 
    // holds initially. The argument eps specifies the required accuracy.
    static double findZero(double a, double b, double eps) {
        assert a < b           : "a has to be less than b";
        assert f(a) * f(b) < 0 : "no sign change in interval [a, b]";
        int     count = 0;
        double  fa    = f(a); ++count;
        double  fb    = f(b); ++count;
        boolean sign  = (fa < 0);
        for (int i = 1; fa != 0.0 && fb != 0.0 && b - a > eps; ++i) {
            double c  = 0.5 * (a + b);
            double fc = f(c); ++count;
            System.out.printf(java.util.Locale.ENGLISH,
                              "%3d: a = %-12.9f, b = %-12.9f, c = %-12.9f, " +
                              "f(a) = %-10.8e, f(b) = %-10.8e, f(c) = %-10.8e, b - a = %-10e\n",
                              i, a, b, c, f(a), f(b), f(c), b - a);
            if ((sign && fc < 0.0) || (!sign && fc > 0)) {
                a = c; fa = fc; 
            } else {
                b = c; fb = fc; 
            }
        }
        System.out.println("number of function evaluations: " + count);
        return 0.5 * (a + b);
    }

    static double f(double x) {
        return x - cos(x);
        //return cos(x) - x;
        //return x * x * x * x - 1;
    }

    public static void main(String args[]) {
        System.out.println("Zero is: " + findZero(0.0, 1.0, 0.5e-9) );
    }
    
}

            