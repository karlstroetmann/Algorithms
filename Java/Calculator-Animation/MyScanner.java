import java.io.*;
import java.util.*;

public class MyScanner
{
	private ArrayStack<Object> mTokenStack;

	public MyScanner(ArrayList<Object> tokenList) {
		calcUtil cU = new calcUtil();
		mTokenStack = new ArrayStack<Object>();
		for (int i = tokenList.size() - 1; i >= 0; --i) {
			if(tokenList.get(i) instanceof Double) {
			    Double Zahl = cU.roundTo6Digits((Double) tokenList.get(i));
				mTokenStack.push(Zahl);
			}else {
				mTokenStack.push(tokenList.get(i));	
			}
		}
		
	}
	
	public MyScanner(InputStream stream)
	{
		ArrayList<Object> tokenList = new ArrayList<Object>();
		System.out.println( "Enter arithmetic expression. " + 
							"Separate Operators with white space:");
		Scanner scanner = new Scanner(stream);
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

	public ArrayStack<Object> getTokenStack() 
	{
		return mTokenStack;
	}
}