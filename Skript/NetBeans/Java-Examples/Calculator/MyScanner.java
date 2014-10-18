import java.math.*;
import java.io.*;
import java.util.*;

public class MyScanner
{
	private ArrayStack<Object> mTokenStack;

	public MyScanner(InputStream stream)
	{
		ArrayList<Object> tokenList = new ArrayList<Object>();
		System.out.println( "Enter arithmetic expression. " + 
							"Separate Operators with white space:");
		Scanner scanner = new Scanner(stream);
		while (scanner.hasNext()) {
			if (scanner.hasNextBigInteger()) {
				tokenList.add(scanner.nextBigInteger());
			} else {
				tokenList.add(scanner.next());
			}
		}
		mTokenStack = new ArrayStack<Object>();
		for (int i = tokenList.size() - 1; i >= 0; --i) {
			mTokenStack.push(tokenList.get(i));
		}
	}

	public ArrayStack<Object> getTokenStack() 
	{
		return mTokenStack;
	}
}

	
		
			
		