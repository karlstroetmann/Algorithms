public class TestHashMap 
{
    public static void main(String[] args) {
        MyHashMap<Integer, Pair<Integer, Integer>> hashMap = 
            new MyHashMap<Integer, Pair<Integer, Integer>>(0);
        for (int i = 0; i < 10; ++i) {
            hashMap.insert(i, new Pair<Integer, Integer>(i, i * i));
        }
        for (int i = 0; i < 10; ++i) {
            System.out.println(hashMap.find(i));
        }
        for (int i = 0; i < 10; ++i) {
            hashMap.delete(i);
        }
        for (int i = 0; i < 10; ++i) {
            System.out.println(hashMap.find(i));
        }
    }
}
