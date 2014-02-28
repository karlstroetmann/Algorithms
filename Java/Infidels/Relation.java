// A relation represents a set of pairs.
public class Relation<E extends Comparable<? super E>>
{
    // mR is the set of pairs that define the relation.
    ComparableSet<Pair<E, E>> mR;
    
    public Relation(ComparableSet<Pair<E, E>> r) {
        mR = r;
    }

    // This method find a path from start to goal.
    public ComparableList<E> findPath(E start, E goal) {
        ComparableList<E> first = new ComparableList<E>();
        first.add(start);
        ComparableSet<ComparableList<E>> p    =
			ComparableSet.singleton(first);
        ComparableSet<ComparableList<E>> oldP = null;
        while (true) {
            oldP = p;
            p    = p.union(pathProduct(p));
            for (ComparableList<E> l : p) {
                if (l.getLast().compareTo(goal) == 0) {
                    return l;
                }
            }
            if (p.compareTo(oldP) == 0) {
                return null;
            } 
        }
    }

    // Compute the path product of P with the relation mR.
    private ComparableSet<ComparableList<E>> 
        pathProduct(ComparableSet<ComparableList<E>> P) 
    {
        ComparableSet<ComparableList<E>> result = 
            new ComparableSet<ComparableList<E>>();
        for (ComparableList<E> p : P) {
            for (Pair<E, E> q : mR) {
                if (p.getLast().compareTo(q.getFirst()) == 0) {
                    ComparableList<E> pq = new ComparableList<E>(p);
                    E second = q.getSecond();
                    pq.add(second);
                    if (!cyclic(pq)) {
                        result.add(pq);
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

        