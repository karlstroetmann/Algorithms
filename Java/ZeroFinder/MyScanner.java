import java.math.*;
import java.io.*;
import java.util.*;

public class MyScanner
{
	private ArrayStack<Object> mTokenStack;
	private Double             mLeft;
	private Double             mRight;

	public MyScanner(InputStream stream)
	{
		ArrayList<Object> tokenList = new ArrayList<Object>();
		System.out.println( "Enter left and right bound and arithmetic expression. " + 
							"Separate Operators with white space:");
		Scanner scanner = new Scanner(stream);
		mLeft  = scanner.nextDouble();
		mRight = scanner.nextDouble();
		while (scanner.hasNext()) {
			if (scanner.hasNextDouble()) {
				tokenList.add(scanner.nextDouble());
			} else {
				tokenList.add(scanner.next());
			}
		}
		mTokenStack = new ArrayStack<Object>();
		for (int i = tokenList.size() - 1; i >= 0; --i) {
			mTokenStack.push(tokenList.get(i));
		}
	}

	public ArrayStack<Object> getTokenStack() {
		return mTokenStack;
	}
	public Double getLeft() {
		return mLeft;
	}
	public Double getRight() {
		return mRight;
	}
}

	
		
			
		