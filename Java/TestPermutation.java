public class TestPermutation {
    public static void main(String[] args) {
	int x =  2000000456;
	int y =  2000000123;
	System.out.println("x = " + x);
	System.out.println("y = " + y);
	x = x - y;
	y = y + x;
	x = y - x;
	System.out.println("x = " + x);
	System.out.println("y = " + y);
    }
}