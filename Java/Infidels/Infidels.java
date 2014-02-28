






public class Infidels
{    
    public static void main(String args[]) {
        ComparableSet<Triple<Integer, Integer, Integer>> P1 = new ComparableSet<Triple<Integer, Integer, Integer>>();
	for (int m = 0; m <= 3; ++m) {
	    for (int k = 0; k <= 3; ++k) {
		if (!problem(m, k) && !problem(3 - m, 3 - k)) {
		    P1.add(new Triple<Integer, Integer, Integer>(m, k, 1));
		}
	    }
	}
        ComparableSet<Triple<Integer, Integer, Integer>> P2 = new ComparableSet<Triple<Integer, Integer, Integer>>();
	for (int m = 0; m <= 3; ++m) {
	    for (int k = 0; k <= 3; ++k) {
		if (!problem(m, k) && !problem(3 - m, 3 - k)) {
		    P2.add(new Triple<Integer, Integer, Integer>(m, k, 0));
		}
	    }
	}
	ComparableSet<Pair<Triple<Integer, Integer, Integer>, Triple<Integer, Integer, Integer>>> r = new ComparableSet<Pair<Triple<Integer, Integer, Integer>, Triple<Integer, Integer, Integer>>>();	
        // { [[M,K,1], [M-PM,K-PK,0]] : [M,K,B] in P, PM in {0..2}, PK in {0..2} 
        //                            | PM + PK in {1,2}    and
        //                              PM <= M             and
        //                              PK <= K             and  
        //                              [M-PM, K-PK,0] in P1 
        // }  
        for (Triple<Integer, Integer, Integer> mkb: P1) {
	    for (int pm = 0; pm <= 2; ++pm) {
		for (int pk = 0; pk <= 2; ++pk) {
		    int m = mkb.getFirst();
		    int k = mkb.getSecond();
		    if (1 <= pm + pk && pm + pk <= 2 && pm <= m && pk <= k) 
		    {
			Triple<Integer, Integer, Integer> n = new Triple<Integer, Integer, Integer>(m - pm, k - pk, 0);
			if (P2.member(n)) {
			    r.add(new Pair<Triple<Integer, Integer, Integer>, Triple<Integer, Integer, Integer>>(mkb, n));
			}
		    }
		}
	    }
	}   
        // { [[M,K,0], [M+PM,K+PK,1]] : [M,K,B] in P, PM in {0..2}, PK in {0..2} 
        //                            | PM + PK in {1,2}    and
        //                              PM <= 3 - M         and
        //                              PK <= 3 - K         and  
        //                              [M+PM, K+PK,1] in P2 
        // }  
        for (Triple<Integer, Integer, Integer> mkb: P2) {
	    for (int pm = 0; pm <= 2; ++pm) {
		for (int pk = 0; pk <= 2; ++pk) {
		    int m = mkb.getFirst();
		    int k = mkb.getSecond();
		    if (1 <= pm + pk && pm + pk <= 2 && pm <= 3-m && pk <= 3-k)
		    {
			Triple<Integer, Integer, Integer> n = new Triple<Integer, Integer, Integer>(m + pm, k + pk, 1);
			if (P1.member(n)) {
			    r.add(new Pair<Triple<Integer, Integer, Integer>, Triple<Integer, Integer, Integer>>(mkb, n));
			}
		    }
		}
	    }
	}   
	Triple<Integer, Integer, Integer> start = new Triple<Integer, Integer, Integer>(3, 3, 1);
	Triple<Integer, Integer, Integer> goal  = new Triple<Integer, Integer, Integer>(0, 0, 0);
    	Relation<Triple<Integer, Integer, Integer>> relation = new Relation(r);
        ComparableList<Triple<Integer, Integer, Integer>> path = relation.findPath(start, goal);
        System.out.println("\n\nLösung:\n");
        for (int i = 0; i < path.size(); ++i) {
	    Triple<Integer, Integer, Integer> left = path.get(i);
	    int m = left.getFirst();
	    int k = left.getSecond();
	    int b = left.getThird();
	    Triple<Integer, Integer, Integer> right = new Triple<Integer, Integer, Integer>(3 - m, 3 - k, 1 - b);
            System.out.println(left + " ~~~ " + right);
	    if (i + 1 < path.size()) {
		Triple<Integer, Integer, Integer> next = path.get(i + 1);
		if (b == 1) {
		    int mb = m - next.getFirst();
		    int kb = k - next.getSecond();
		    System.out.println("   >>> { " + mb + ", " + kb + " } >>>");
		} else {
		    int mb = next.getFirst()  - m;
		    int kb = next.getSecond() - k;
		    System.out.println("   <<< { " + mb + ", " + kb + " } <<<");
		}
	    }
	}
    }

    static boolean problem(int m, int k) {
	return m > 0 && m < k;
    }
}



    
