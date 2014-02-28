import java.util.*;

public class StackTest 
{
    public static void main(String[] args) {
        Stack<Integer> stack = new ArrayStack<Integer>();
        for (int i = 0; i < 10; ++i) {
            stack.push(i);
            System.out.println(stack);
        }
        for (int i = 0; i < 11; ++i) {
            System.out.println(i + ":" + stack.top());
            stack.pop();
            System.out.println(stack);
        }
    }
}
