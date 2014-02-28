




public class WolfZiegeKohl 
{    
    public static void main(String args[]) {
        // all := { "Bauer", "Wolf", "Ziege", "Kohl" };
        ComparableSet<String> all = new ComparableSet<String>();
        all.add("Bauer");
        all.add("Wolf");
        all.add("Ziege");
        all.add("Kohl");
    	// P := pow All;
        ComparableSet<ComparableSet<String>> p = all.powerSet();
        ComparableSet<Pair<ComparableSet<String>, ComparableSet<String>>> r = new ComparableSet<Pair<ComparableSet<String>, ComparableSet<String>>>();
        // R1 := { [ S, S - B ] : S in P, B in pow S |
        //         "Bauer" in B and #B <= 2 and not problem(S - B) 
        //       };
        for (ComparableSet<String> s: p) {
            for (ComparableSet<String> b : s.powerSet()) {
	         	ComparableSet<String> sb = s.difference(b);
                if ( b.member("Bauer")          && 
                     b.size() <= 2              && 
                     !problem(sb)
                    ) 
                {
                    Pair<ComparableSet<String>, ComparableSet<String>> ssb = new Pair<ComparableSet<String>, ComparableSet<String>>(s, sb);
                    r.add(ssb); 
                }
            }
        }
        // R2 := { [ S, S + B ] : S in P, B in pow (All - S) |
        //         "Bauer" in B and #B <= 2 and not problem(All - S - B) 
        //        };
        for (ComparableSet<String> s: p) {
	        ComparableSet<String> as = all.difference(s);
            for (ComparableSet<String> b : as.powerSet()) {
                if ( b.member("Bauer")          && 
                     b.size() <= 2              && 
                     !problem(as.difference(b))
                   ) 
                {
		            ComparableSet<String> sb = s.union(b);
                    Pair<ComparableSet<String>, ComparableSet<String>> ssb = new Pair<ComparableSet<String>, ComparableSet<String>>(s, sb);
                    r.add(ssb); 
                }
            }
        }
        for (Pair<ComparableSet<String>, ComparableSet<String>> xy: r) {
            System.out.println(xy);
        }

        ComparableSet<String> goal = new ComparableSet<String>();
    	Relation<ComparableSet<String>> relation = new Relation(r);
        ComparableList<ComparableSet<String>> path = relation.findPath(all, goal);
        System.out.println("\n\nLösung:\n");
        for (ComparableSet<String> left : path) {
	    ComparableSet<String> right = all.difference(left);
            System.out.println(left + ", " + right);
        }
    }

    static boolean problem(ComparableSet<String> s) {
        return (s.member("Ziege") && s.member("Kohl")) || 
               (s.member("Wolf") && s.member("Ziege"));
    }
}



    
