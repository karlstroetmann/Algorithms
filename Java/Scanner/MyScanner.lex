import java.util.*;
import java.math.*;
import java.io.*;

abstract class Token {}

class Operator extends Token 
{
	String mString;
	
	Operator(String string) {
		mString = string;
	}

	public String toString() 
	{
		return mString;
	} 
}

class Zahl extends Token 
{
	BigInteger mZahl;
	
	Zahl(String zahl) {
		mZahl = new BigInteger(zahl);
	}

	public BigInteger getZahl() {
		return mZahl;
	}

	public String toString() 
	{
		return mZahl.toString();
	}
}

%%

%{
	public Stack<Token> run() throws IOException {
		Token t;
		ArrayList<Token> tokenArray = new ArrayList<Token>();
		while ((t = yylex()) != null) {
			tokenArray.add(t);
		}
		ArrayStack<Token> result = new ArrayStack<Token>();
		for (int i = tokenArray.size() - 1; i >= 0; --i) {
			result.push(tokenArray.get(i));
		}	
		return result;
	}

%}

%public
%class MyScanner
%type Token

%%

[0-9]+      { return new Zahl(yytext()); }

"**"        { return new Operator(yytext()); }

[ \t\n]+    {} 

.           { return new Operator(yytext()); } 
	

	 
	
	 
	
