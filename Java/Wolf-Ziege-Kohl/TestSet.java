public class TestSet 
{
    public static void main(String[] args) {
        System.out.println("\n");    
        basicTest();
        composeTest();
        transformTest();
        selectTest();
        combinatorTest();
    }

    static void basicTest() {        
        ComparableSet<Integer> s1 = ComparableSet.range(0, 3);
        ComparableSet<Integer> s2 = ComparableSet.range(2, 5);
        System.out.println(s1 + " + " + s2 + " = " + s1.union(s2));
        System.out.println(s1 + " * " + s2 + " = " + s1.intersection(s2));
        System.out.println(s1 + " - " + s2 + " = " + s1.difference(s2));
        System.out.println(s1 + " x " + s2 + " = " + s1.product(s2));
        System.out.println("powerset(" + s1 + ") = " + s1.powerSet());    
        System.out.println("s1 = " + s1);    
        System.out.println("\n");    
    }
    
    static void composeTest() {
        ComparableSet<Integer> s1 = ComparableSet.range(0,1);
        ComparableSet<Integer> s2 = ComparableSet.range(1,2);
        ComparableSet<Pair<Integer, Integer>> r1 = s1.product(s2);
        ComparableSet<Pair<Integer, Integer>> r2 = s2.product(s1);
        System.out.println( r1 + " o " + r2 + " = " + 
                            ComparableSet.compose(r1, r2) );
        System.out.println( r2 + " o " + r1 + " = " + 
                            ComparableSet.compose(r2, r1) );
        System.out.println("\n");    
    }
    
    static void transformTest()    {
        // primes := { 2..n } - { p*q | p in { 2..n }, q in { 2..n} };
        ComparableSet<Integer> numbers = ComparableSet.range(2, 100);
        ComparableSet<Pair<Integer, Integer>> pairs = numbers.product(numbers);
        ComparableSet<Integer> products = pairs.transform(new Multiplier());
        ComparableSet<Integer> primes   = numbers.difference(products);
        System.out.println("Primes, first try:  " + primes);
        System.out.println("\n");    
    }
    
    static void selectTest() {
        // primes := { p in  {2..n} | { x in {2..p-1} | p mod x = 0 } = {} };
        ComparableSet<Integer> primes2 = ComparableSet.range(2, 100);
        primes2 = primes2.select(new Selector<Integer>() {
            public boolean select(Integer p) {
                ComparableSet<Integer> dividers = ComparableSet.range(2, p-1);
                DividerSelector dividerSelector = new DividerSelector(p);
                dividers = dividers.select(dividerSelector);
                return dividers.isEmpty();
            }
        });
        System.out.println("Primes, second try: " + primes2);
        System.out.println("\n");    
    }
    
    static void combinatorTest() {
        ComparableSet<Integer> primes3 = ComparableSet.range(2, 100);
        ComparableSet<Integer> setA    = ComparableSet.range(2, 100);
        ComparableSet<Integer> setB    = ComparableSet.range(2, 100);
        ComparableSet<Integer> prods   = 
            ComparableSet.combineSets(setA, setB, new 
                Combinator<Integer, Integer, Integer>()
                {
                    public Integer combine(Integer x, Integer y) {
                        return x * y;
                    }
                });
        primes3 = primes3.difference(prods);
        System.out.println("Primes, third try:  " + primes3);        
    }
    
}

class DividerSelector implements Selector<Integer>
{
    int mDividend;
    
    DividerSelector(int dividend) {
        mDividend = dividend;
    }

    public boolean select(Integer x) {
        return mDividend % x == 0;
    }
}

class Multiplier implements Transformer<Integer, Pair<Integer, Integer>>
{
    public Integer transform(Pair<Integer, Integer> pair) {
        return pair.getFirst() * pair.getSecond();
    }
}              

