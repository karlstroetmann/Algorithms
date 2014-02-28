public class TestDistanceRelation 
{
    public static void main(String[] args) {
        ComparableSet<Pair<Pair<Integer, Integer>, Integer>> r = 
            new ComparableSet<Pair<Pair<Integer, Integer>, Integer>>();

        r.add(makePairOfPair(1,2, 1));
        r.add(makePairOfPair(2,4, 3));
        r.add(makePairOfPair(2,6, 1));
        r.add(makePairOfPair(6,4, 1));
        r.add(makePairOfPair(1,4, 4));
        r.add(makePairOfPair(4,5, 3));

        DistanceRelation<Integer> R = new DistanceRelation<Integer>(r);
        ComparableList<Integer> path = R.findPath(1,5, 7);
        System.out.println(path);
    }    

    static Pair<Pair<Integer, Integer>, Integer> makePairOfPair(int i, int j, int k) {
        Pair<Integer, Integer> pair = new Pair<Integer, Integer>(i, j);
        return new Pair<Pair<Integer, Integer>, Integer>(pair, k);
    }
}
