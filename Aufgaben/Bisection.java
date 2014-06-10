import java.util.*;
import static java.lang.Math.*;

public class Bisection {
    Calculator mCalculator;

    public Bisection() {
        mCalculator = new Calculator();        
    }

    // This function locates the zero of the function f in the intervall [a, b].
    // It is assumed that a < b and that the function changes the sign in the
    // interval [a, b], i.e. either 
    //        f(a) < 0 and f(b) > 0 or f(a) > 0 and f(b) < 0 
    // holds initially. The argument eps specifies the required accuracy.
    double findZero(double a, double b, double eps) {
        assert a < b           : "a has to be less than b";
        assert f(a) * f(b) < 0 : "no sign change in interval [a, b]";
        int     count = 0;
        double  fa    = f(a); ++count;
        double  fb    = f(b); ++count;
        boolean sign  = (fa < 0);
        for (int i = 1; fa != 0.0 && fb != 0.0 && b - a > eps; ++i) {
            double c  = 0.5 * (a + b);
            double fc = f(c); ++count;
            if ((sign && fc < 0.0) || (!sign && fc > 0)) {
                a = c; fa = fc; 
            } else {
                b = c; fb = fc; 
            }
        }
        return 0.5 * (a + b);
    }

    double f(double x) {
        return mCalculator.evaluate(x);
    }

    public static void main(String args[]) {
        Bisection b     = new Bisection();
        Double    left  = b.mCalculator.mLeft;
        Double    right = b.mCalculator.mRight;
        System.out.println("Zero is: " + b.findZero(left, right, 0.5e-9) );
    }
    
}

            