import java.util.*;
import java.math.*;

public class Calculator 
{
    Stack<BigInteger> mArguments;
    Stack<String>     mOperators;
    Stack<Object>     mTokenStack;
    
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
        if (operator.equals("+") || operator.equals("-")) {
            return 1;
        } else if ( operator.equals("*") || operator.equals("/") || 
                    operator.equals("%")) {
            return 2;
        } else if (operator.equals("**") || operator.equals("^")) {
            return 3;
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

    // This methods removes two arguments from the argument stack and combines
    // these arguments according to the operator on top of the operator stack.
    // This operator is then removed from the operator stack and the result of
    // the computation is pushed onto the argument stack.
    void popAndEvaluate() 
    {
        BigInteger rhs = mArguments.top();
        mArguments.pop();
        BigInteger lhs = mArguments.top();
        mArguments.pop();
        String operator = mOperators.top();
        mOperators.pop();
        BigInteger result = null;
        if (operator.equals("+")) {
            result = lhs.add(rhs);
        } else if (operator.equals("-")) {
            result = lhs.subtract(rhs);
        } else if (operator.equals("*")) {
            result = lhs.multiply(rhs);
        } else if (operator.equals("/")) {
            result = lhs.divide(rhs);
        } else if (operator.equals("%")) {
            result = lhs.remainder(rhs);
        } else if (operator.equals("**") || operator.equals("^")) {
            result = lhs.pow(rhs.intValue());       
        } else {
            System.out.println("ERROR: *** Unknown Operator ***");
            System.exit(1);
        }
        mArguments.push(result);
    }

    public Calculator()
    {
        MyScanner scanner = new MyScanner(System.in);
        mTokenStack = scanner.getTokenStack();
        mArguments  = new ArrayStack<BigInteger>();
        mOperators  = new ArrayStack<String>();
        while (!mTokenStack.isEmpty()) {
            if (mTokenStack.top() instanceof BigInteger) {
                BigInteger number = (BigInteger) mTokenStack.top();
                mTokenStack.pop();
                mArguments.push(number);
                continue;
            } 
            String nextOp = (String) mTokenStack.top();
            mTokenStack.pop();
            if (mOperators.isEmpty() || nextOp.equals("(")) {
                mOperators.push(nextOp);
                continue;
            }
            String stackOp = mOperators.top();
            if (stackOp.equals("(") && nextOp.equals(")") ) {
                mOperators.pop();
            } else if (nextOp.equals(")")) {
                popAndEvaluate();
                mTokenStack.push(nextOp);
            } else if (evalBefore(stackOp, nextOp)) {
                popAndEvaluate();
                mTokenStack.push(nextOp);
            } else {
                mOperators.push(nextOp);
            }
        }
        while (!mOperators.isEmpty()) {
            popAndEvaluate();
        }
        BigInteger result = mArguments.top();
        mArguments.pop();
        assert mArguments.isEmpty() && mOperators.isEmpty() : "*** Syntax Error ***";
        System.out.println("The result is: " + result);
    }

    public static void main(String[] args) 
    {
        Calculator calc = new Calculator();
    }    
}

