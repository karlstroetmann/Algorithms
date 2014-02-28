public class TestRelation 
{
    public static void main(String[] args) {
        ComparableSet<Pair<Integer, Integer>> r = 
            new ComparableSet<Pair<Integer, Integer>>();
        r.add(new Pair<Integer, Integer>(1,2));
        r.add(new Pair<Integer, Integer>(1,3));
        r.add(new Pair<Integer, Integer>(3,1));
        r.add(new Pair<Integer, Integer>(2,3));
        r.add(new Pair<Integer, Integer>(3,4));
        r.add(new Pair<Integer, Integer>(4,5));
        Relation<Integer> R = new Relation<Integer>(r);
        ComparableList<Integer> path = R.findPath(1,5);
        System.out.println(path);
    }    
}
