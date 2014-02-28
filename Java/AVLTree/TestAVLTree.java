import java.util.*; 

/** \class TestBinaryTree
 *  A class to test the implementation of binary trees.  The test
 *  consists of three steps:
 *    - we build a list of random numbers,
 *    - we insert these random numbers into a new binary tree,
 *    - we convert the binary tree into a list, which should then be sorted.
 *  .
 */
public class TestAVLTree 
{
    public static void main(String[] args) {
        Random randomGen = new Random(0);
        LinkedList<Integer> l = new LinkedList<Integer>();
        for (int i = 0; i < 100; ++i) {
            l.addLast(randomGen.nextInt(1000));
        }
        System.out.println(l);
        System.out.println(sort(l));
        
    }

    static <Key extends Comparable<Key>> 
        LinkedList<Key> sort(LinkedList<Key> list) 
    {
        AVLTree<Key, Integer> b = new AVLTree<Key, Integer>();
        for (Key key : list) {
            b.insert(key, 0);
        }
        return b.toList();
    }
    

}
