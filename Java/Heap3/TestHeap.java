import java.util.*;

public class TestHeap 
{
    public static void main(String[] args) 
    {
        Random                     random   = new Random(1);
        HeapTree<Integer, Integer> heapTree = new HeapTree<Integer, Integer>();
        int n = 100;
        for (int i = 0; i < n; ++i) {
            Integer k = random.nextInt(100);
            heapTree.insert(k, k);
            //        System.out.println(heapTree);
            assert heapTree.isHeap()     : "tree is not a heap!"  ;
            assert heapTree.isBalanced() : "tree is not balanced!";
        }
        for (int i = 0; i < n; ++i) {
            System.out.println(heapTree.top());
            heapTree.remove();
            //        System.out.println(heapTree);
            assert heapTree.isHeap()     : "tree is not a heap!"  ;
            assert heapTree.isBalanced() : "tree is not balanced!";
        }
    }
}
