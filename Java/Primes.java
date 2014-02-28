import java.util.*;

public class Primes {

    // compute the set { low .. high }
    static Set<Integer> range(int low, int high) {
        Set<Integer> result = new TreeSet<Integer>();
        for (int i = low; i <= high; ++i) {
            result.add(i);
        }
        return result;
    }

    // compute the set { p * q : p in s1, q in s2 }
    static Set<Integer> products(Set<Integer> s1, Set<Integer> s2) {
        Set<Integer> result = new TreeSet<Integer>();
        for (Integer p : s1) {
            for (Integer q : s2) {
                result.add(p * q);
            }
        }
        return result;
    }

    // compute the set of primes up to n
    // primes = { 2 .. n } - { p * q : p in { 2 .. n }, q in { 2 .. n } }
    static Set<Integer> primes(int n) {
        Set<Integer> primes   = range(2, n);
        Set<Integer> numbers  = range(2, n);
        Set<Integer> products = products(numbers, numbers);
        primes.removeAll(products);
        return primes;
    }

    public static void main(String[] args) {
		int n = 100;
		if (args.length == 1) {
			n = Integer.parseInt(args[0]);
		}
		Set<Integer> primes = primes(n);
        String all = "{ ";
        boolean first = true;
		for (Integer p: primes) {
            if (first) {
                all += p;
                first = false;
            } else {
                all += ", " + p;
            }        
		}
        all += " }";
        System.out.println(all);
    }
}