// A relation represents a set of pairs.
public class DistanceRelation<E extends Comparable<? super E>>
{
    // mR is the set of pairs that define the relation.
    ComparableSet<Pair<Pair<E, E>, Integer>> mR;
    
    public DistanceRelation(ComparableSet<Pair<Pair<E, E>, Integer>> r) {
        mR = r;
    }

    // This method find a path from start to goal.
    // limit ist the number of iterations.
    public ComparableList<E> findPath(E start, E goal, int limit) {
        ComparableList<E> startList = new ComparableList<E>();
        startList.add(start);
        Pair<ComparableList<E>, Integer> startListPair =
            new Pair<ComparableList<E>, Integer>(startList, 0);
        ComparableSet<Pair<ComparableList<E>, Integer>> pathSet = 
            new ComparableSet<Pair<ComparableList<E>, Integer>>();
        pathSet.add(startListPair);
        for (int i = 0; i < limit; ++i) {
            pathSet = pathSet.union(pathProduct(pathSet));
        }
        Integer           minDauer = null;
        ComparableList<E> bestPath = null;
        for (Pair<ComparableList<E>, Integer> pathPair : pathSet) {
            ComparableList<E> path  = pathPair.getFirst();
            Integer           dauer = pathPair.getSecond();
            if (goal.compareTo(path.getLast()) == 0) {
                if (minDauer == null || dauer < minDauer) {
                    minDauer = dauer;
                    bestPath = path;
                }
            }
        }
        return bestPath;
    }

    // Compute the path product of P with the relation mR.
    private ComparableSet<Pair<ComparableList<E>, Integer>> 
        pathProduct(ComparableSet<Pair<ComparableList<E>, Integer>> P) 
    {
        ComparableSet<Pair<ComparableList<E>, Integer>> result = 
            new ComparableSet<Pair<ComparableList<E>, Integer>>();
        for (Pair<ComparableList<E>, Integer> pl : P) {
            for (Pair<Pair<E, E>, Integer> ql : mR) {
                ComparableList<E> p  = pl.getFirst();
                Integer           l1 = pl.getSecond();
                Pair<E,E>         q  = ql.getFirst();
                Integer           l2 = ql.getSecond();                
                if (p.getLast().compareTo(q.getFirst()) == 0) {
                    ComparableList<E> pq = new ComparableList<E>(p);
                    E second = q.getSecond();
                    pq.add(second);
                    if (!cyclic(pq) && pq.size() <= 8) {
                        // Th test pq.size() <= 8 is used to make the problem 
                        // tractable. Semantically, the test is not legitimate.
                        Pair<ComparableList<E>, Integer> pql = 
                            new Pair<ComparableList<E>, Integer>(pq, l1 + l2);
                        result.add(pql);
                    }
                }
            }
        }
        return result;
    }
    
    // check whether the same element occurs multiple times in the list l
    private static <T extends Comparable<? super T>>
            boolean cyclic(ComparableList<T> l) 
    {
        ComparableSet<T> all = new ComparableSet<T>();
        for (T x : l) {
            all.add(x);
        }
        return all.size() < l.size();
    }    

}

        