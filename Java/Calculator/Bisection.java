public class Bisection {
    Calculator mCalculator;

    public Bisection() {
        MyScanner scanner = new MyScanner(System.in);
        mCalculator       = new Calculator(scanner.getTokenStack());
        Double left       = scanner.getLeft();
        Double right      = scanner.getRight();
        System.out.println("Zero at: " + findZero(left, right, 0.5e-12));
    }

    // This function locates the zero of the function f in the intervall [a, b].
    // It is assumed that a < b and that the function changes the sign in the
    // interval [a, b], i.e. that either 
    //        f(a) < 0 and f(b) > 0 or f(a) > 0 and f(b) < 0 
    // holds initially. The argument eps specifies the required accuracy:
    // The distance between the returned solution and the real root is guaranteed
    // to be less than eps.  Furthermore, the algorithm is stable: The number of
    // steps required is given by log2( (b-a)/eps ).
    double findZero(double a, double b, double eps) {
        double fa = f(a);
        double fb = f(b);
        assert a < b       : "a has to be less than b";
        assert fa * fb < 0 : "no sign change in interval [a, b]";
        while (b - a > eps) {
            double c  = 0.5 * (a + b);
            double fc = f(c);
            if (fa < 0 && fc < 0 || fa > 0 && fc > 0) {
                a = c; 
            } else {
                b = c; 
            }
        }
        return 0.5 * (a + b);
    }

    double f(double x) {
        return mCalculator.evaluate(x);
    }

    public static void main(String args[]) {
        new Bisection();
    }
    
}

            