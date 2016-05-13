import java.util.*;


public class Calculator 
{
    Stack<Double>     mArguments;
    Stack<String>     mOperators;
    Stack<Object>     mTokenStack;
    private static ArrayList<Object> tokenList;
    private boolean isRationalFunction = false;
    private static boolean stepMode = false;
    
    OutputGUI Output; 
    
    // Return true if operator op1 should be evaluated before operator op2.
    static boolean evalBefore(String op1, String op2)
    {
        if (op1.equals("(")) {
            return false;
        }
        if (precedence(op1) > precedence(op2)) {
            return true;
        } else if (precedence(op1) == precedence(op2)) {
            return op1.equals(op2) ? isLeftAssociative(op1) : true;
        } else {
            return false;
        }
    }       

    static int precedence(String operator)
    {
        if(operator.equals("+") || operator.equals("-")) {
            return 1;
        } else if ( operator.equals("*") || operator.equals("/") || 
                    operator.equals("%")) {
            return 2;
        } else if (operator.equals("**") || operator.equals("^")) {
            return 3;
        } else if(operator.equals("sqrt") || operator.equals("exp") ||
	          operator.equals("log") || operator.equals("sin") ||
	          operator.equals("cos") || operator.equals("tan"))
	{
	    return 4;	
	} else {
            System.out.println("ERROR: *** unkown operator *** ");
        }
        System.exit(1);
        return 0;
    }

    static boolean isLeftAssociative(String operator)
    {
        if (operator.equals("+") || operator.equals("-") ||
            operator.equals("*") || operator.equals("/") || 
            operator.equals("%")) {
            return true;
        } else if (operator.equals("**") || operator.equals("^")) {
            return false;
        } else {
            System.out.println("ERROR: *** unkown operator *** ");
        }
        System.exit(1);
        return false;
    }
    
    Boolean operatorType(String operator) 
    { 
    	if (operator.equals("+") || operator.equals("-") || operator.equals("*") ||
	    operator.equals("/") || operator.equals("**"))
	{
		return true;
	}
	return false;    
    }
    
    // This methods removes two arguments from the argument stack and combines
    // these arguments according to the operator on top of the operator stack.
    // This operator is then removed from the operator stack and the result of
    // the computation is pushed onto the argument stack.
    void popAndEvaluate() 
    {
    	String operator = mOperators.top();
        mOperators.pop();
        Output.moveOperantToCalculate();
	Double rhs = mArguments.top();
        mArguments.pop();
        Output.moveArgumentToCalculate(true);
	Double lhs = 0D;
	boolean LHS;
	if (LHS = operatorType(operator))
	{
        	lhs = mArguments.top();
        	mArguments.pop();
        	Output.moveArgumentToCalculate(false);
	}
        Double result = null;
        if (operator.equals("+")) {
            result = lhs + rhs;
        } else if (operator.equals("-")) {
            result = lhs - rhs;
        } else if (operator.equals("*")) {
            result = lhs * rhs;
        } else if (operator.equals("/")) {
            result = lhs / rhs;
        } else if (operator.equals("**") || operator.equals("^")) {
            result = Math.pow(lhs,rhs.intValue());       
        } else if (operator.equals("sqrt")) {
            result = Math.sqrt(rhs.intValue());       
        } else if (operator.equals("exp")) {
            result = Math.exp(rhs.intValue());       
        } else if (operator.equals("log")) {
            result = Math.log(rhs.intValue());       
        } else if (operator.equals("sin")) {
            result = Math.sin(rhs.intValue());       
        } else if (operator.equals("cos")) {
            result = Math.cos(rhs.intValue());       
        } else if (operator.equals("tan")) {
            result = Math.tan(rhs.intValue());       
        } else {
            System.out.println("ERROR: *** Unknown Operator ***");
            System.exit(1);
        }
        calcUtil cU = new calcUtil();
        result = cU.roundTo6Digits(result);
        mArguments.push(result);
       	Output.addErgebnis(result,LHS);
    }
    
    public boolean CheckTokenStackforX() {
    	String TestString;
	Stack<Object> TokenStack = null;
	try {
	  TokenStack = mTokenStack.clone();
	} catch(Exception e) {
	  e.printStackTrace();
	}
	
    	while (!TokenStack.isEmpty()) {
            if (TokenStack.top() instanceof Double) {
                TokenStack.pop();
                continue;
            } 
	    TestString = (String) TokenStack.top();
	    if ( TestString.equals("x") ) {
	   	return true;
	    }
	    else {
	      TokenStack.pop();
	    }
	}
	return false;
    }
    
    public void ReadStack(ArrayList<Object> tokenList) {
        MyScanner scanner = new MyScanner(tokenList);
        mTokenStack = scanner.getTokenStack();
        Output.setInputList(mTokenStack);
        mArguments  = new ArrayStack<Double>();
        mOperators  = new ArrayStack<String>();
                
        
	isRationalFunction = CheckTokenStackforX();
	//Debug Out
	System.out.println("Debug Out rational function:" + isRationalFunction);
    }
    
    public void SortArgsAndOps(Double x) {
    	Stack<Object> TokenStack = null;
    	try {
    		TokenStack = mTokenStack.clone();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
	
    	while (!TokenStack.isEmpty()) {
            if (TokenStack.top() instanceof Double) {
                Double number = (Double) TokenStack.top();
                TokenStack.pop();
                mArguments.push(number);
                Output.addArgumentfromInputList();
                                
                continue;
            } 
	    
        String nextOp = (String) TokenStack.top();
	    TokenStack.pop();
	    if(isRationalFunction && nextOp.equals("x")) {
	      mArguments.push(x);
	      continue;	    	
	    }
            if (mOperators.isEmpty() || nextOp.equals("(")) {
            	mOperators.push(nextOp);
            	Output.addOperantfromInputList(nextOp);
	      //System.out.println("DebugOut: Operatoreingabe: " + mOperators.top());
              continue;
            }
            
            
            String stackOp = mOperators.top();
            if (stackOp.equals("(") && nextOp.equals(")") ) {
                Output.addOperantfromInputList(nextOp);
            	mOperators.pop();
                
            } else if (nextOp.equals(")")) {
            	popAndEvaluate();
                TokenStack.push(nextOp);
                
            } else if (evalBefore(stackOp, nextOp)) {
            	popAndEvaluate();
                TokenStack.push(nextOp);
            } else {
                mOperators.push(nextOp);
                Output.addOperantfromInputList(nextOp);
            }
        }
    }
    
    public Double evaluateTerm() {
        while (!mOperators.isEmpty()) {
	    //System.out.println("DebugOut: ausgewerterter Operator: " + mOperators.top());
            popAndEvaluate();
        }
        Double result = mArguments.top();
        mArguments.pop();
        //Output.delArg(false);
        assert mArguments.isEmpty() && mOperators.isEmpty() : "*** Syntax Error ***";
        return result;
    }
    
    public Double f(Double x) {
      //Argumente und Operatoren sortieren
      SortArgsAndOps(x);
      
      return evaluateTerm();
          
    }
    
    // This function locates the zero of the function f in the intervall [a, b].
    // It is assumed that a < b and that the function changes the sign in the
    // interval [a, b], i.e. either 
    //        f(a) < 0 and f(b) > 0 or f(a) > 0 and f(b) < 0 
    // holds initially. The argument eps specifies the required accuracy.
    public double findZero(double a, double b, double eps) {
        assert a < b           : "a has to be less than b";
        assert f(a) * f(b) < 0 : "no sign change in interval [a, b]";
        int     count = 0;
        double  fa    = f(a); ++count;
        double  fb    = f(b); ++count;
        boolean sign  = (fa < 0);
        for (int i = 1; fa != 0.0 && fb != 0.0 && b - a > eps; ++i) {
            double c  = 0.5 * (a + b);
            double fc = f(c); ++count;
            System.out.printf(java.util.Locale.ENGLISH,
                              "%3d: a = %-12.9f, b = %-12.9f, c = %-12.9f, " +
                              "f(a) = %-10.8e, f(b) = %-10.8e, f(c) = %-10.8e, b - a = %-10e\n",
                              i, a, b, c, f(a), f(b), f(c), b - a);
            if ((sign && fc < 0.0) || (!sign && fc > 0)) {
                a = c; fa = fc; 
            } else {
                b = c; fb = fc; 
            }
        }
        System.out.println("number of function evaluations: " + count);
        return 0.5 * (a + b);
    }    
    
     
    public Calculator()
    {
    	tokenList = new ArrayList<Object>();
    	CalcGUI calcframe = new CalcGUI();
    	Output = new OutputGUI();
  
    	while(!calcframe.run()) {
    	};

		tokenList = calcframe.getTokenList();
		
		//Set the OutputFrame to visible
		Output.Visible(true);
    	
    	ReadStack(tokenList);  //Reads the input stack 
    	if( isRationalFunction ) {
    		System.out.println("Zero is: " + findZero(0.0, 1.0, 5e-9) );
    	} else {
    		SortArgsAndOps(0.0);
    		System.out.println("The result is: " + evaluateTerm());
		
    	}
    }
    
    public static boolean getStepMode() {
    	return stepMode;
    }
    
    public static boolean setStepMode(boolean newMode) {
    	return stepMode = newMode;
    }
    
    public static void main(String[] args) {
    	try {
        Calculator calc = new Calculator();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}        
    }    
}

