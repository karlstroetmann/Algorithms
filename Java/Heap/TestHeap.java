import java.util.*;

public class TestHeap 
{
    public static void main(String[] args) 
    {
        Random                     random   = new Random(1);
        HeapTree<Integer, Integer> heapTree = new HeapTree<Integer, Integer>();
        for (int i = 0; i < 10; ++i) {
            Integer k = random.nextInt(100);
            heapTree.insert(k, k);
        }
        for (int i = 0; i < 10; ++i) {
            System.out.println(heapTree.top());
            heapTree.remove();
        }
    }
}
