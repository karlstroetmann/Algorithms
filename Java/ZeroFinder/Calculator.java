import java.util.*;
import java.math.*;

public class Calculator 
{
    Stack<Double> mArguments;
    Stack<String> mOperators;
    Stack<Object> mTokenStack;
    Double        mX = 1.0;
    Double        mLeft;
    Double        mRight;
    
    // Return true if operator stackOp should be evaluated before operator nextOp.
    // stackOp is the next operator on the operator stack, while nextOp is the 
    // next operator from teh token stack.
    static boolean evalBefore(String stackOp, String nextOp) {
        if (stackOp.equals("(")) {
            return false;
        }
        if (isConstOperator(stackOp)) {
            return true;
        }
        if (precedence(stackOp) > precedence(nextOp)) {
            return true;
        } else if (isUnaryOperator(stackOp) && isUnaryOperator(nextOp)) {
            return false;
        } else if (precedence(stackOp) == precedence(nextOp)) {
            return stackOp.equals(nextOp) ? isLeftAssociative(stackOp) : true;
        } else {
            return false;
        }
    }       

    static int precedence(String operator) {
        if (operator.equals("+") || operator.equals("-")) {
            return 1;
        } else if ( operator.equals("*") || operator.equals("/") || 
                    operator.equals("%")) {
            return 2;
        } else if (operator.equals("**") || operator.equals("^")) {
            return 3;
        } else if ( isUnaryOperator(operator) ) {
            return 4;
        } else if ( isConstOperator(operator) ) {
            return 5;
        }
        assert false : "ERROR: Unknown Operator " + operator + " in precedence()";
        return 0;
    }

    static boolean isLeftAssociative(String operator) {
        if (operator.equals("+") || operator.equals("-") ||
            operator.equals("*") || operator.equals("/") || 
            operator.equals("%")) {
            return true;
        } else if (operator.equals("**") || operator.equals("^")) {
            return false;
        }
        assert false : "ERROR: Unknown Operator " + operator + " in isLeftAssociative()";
        return false;
    }

    static boolean isConstOperator(String operator) {
        return operator.equals("x") || operator.equals("e") || operator.equals("Pi");
    }

    static boolean isUnaryOperator(String operator) {
        return operator.equals("sqrt") || operator.equals("exp")  || operator.equals("log")  || 
               operator.equals("sin")  || operator.equals("cos")  || operator.equals("tan")  || 
               operator.equals("atan") || operator.equals("asin") || operator.equals("acos");
    }

    // This methods removes two arguments from the argument stack and combines
    // these arguments according to the operator on top of the operator stack.
    // This operator is then removed from the operator stack and the result of
    // the computation is pushed onto the argument stack.
    void popAndEvaluate() {
        String operator = mOperators.top();
        mOperators.pop();
        Double result = null;
        if (isConstOperator(operator)) {
            if (operator.equals("x")) {
                result = mX;
            } else if (operator.equals("e") ) {
                result = Math.E;
            } else if (operator.equals("Pi")) {
                result = Math.PI;
            }
        } else if (isUnaryOperator(operator)) {
            Double arg = mArguments.top();
            mArguments.pop();
            if (operator.equals("sqrt")) {
                result = Math.sqrt(arg);
            } else if ( operator.equals("exp") ) {
                result = Math.exp(arg);
            } else if ( operator.equals("log") ) {
                result = Math.log(arg);
            } else if ( operator.equals("sin") ) {
                result = Math.sin(arg);
            } else if ( operator.equals("cos") ) {
                result = Math.cos(arg);
            } else if ( operator.equals("tan") ) {
                result = Math.tan(arg);
            } else if ( operator.equals("atan") ) {
                result = Math.atan(arg);
            } else if ( operator.equals("asin") ) {
                result = Math.asin(arg);
            } else if ( operator.equals("acos") ) {
                result = Math.acos(arg);
            }
        } else {
            Double rhs = mArguments.top();
            mArguments.pop();
            Double lhs = mArguments.top();
            mArguments.pop();
            if (operator.equals("+")) {
                result = lhs + rhs;
            } else if (operator.equals("-")) {
                result = lhs - rhs;
            } else if (operator.equals("*")) {
                result = lhs * rhs;
            } else if (operator.equals("/")) {
                result = lhs / rhs;
            } else if (operator.equals("**") || operator.equals("^")) {
                result = Math.pow(lhs, rhs);       
            } else {
                assert false : "ERROR: Unknown Operator " + operator + " in popAndEvaluate()";
            }
        }
        mArguments.push(result);
    }
  
    public Calculator() {
        MyScanner scanner = new MyScanner(System.in);
        mLeft       = scanner.getLeft();
        mRight      = scanner.getRight();
        mTokenStack = scanner.getTokenStack();
    }
    
    public double evaluate(double x) {
        Stack<Object> tokenStack = null;
        try {
            tokenStack = mTokenStack.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }    
        mX         = x;
        mArguments = new ArrayStack<Double>();
        mOperators = new ArrayStack<String>();
        while (!tokenStack.isEmpty()) {
            if (tokenStack.top() instanceof Double) {
                Double number = (Double) tokenStack.top();
                tokenStack.pop();
                mArguments.push(number);
                continue;
            } 
            String nextOp = (String) tokenStack.top();
            tokenStack.pop();
            if (mOperators.isEmpty() || nextOp.equals("(")) {
                mOperators.push(nextOp);
                continue;
            }
            String stackOp = mOperators.top();
            if (stackOp.equals("(") && nextOp.equals(")") ) {
                mOperators.pop();
            } else if (nextOp.equals(")")) {
                popAndEvaluate();
                tokenStack.push(nextOp);
            } else if (evalBefore(stackOp, nextOp)) {
                popAndEvaluate();
                tokenStack.push(nextOp);
            } else {
                mOperators.push(nextOp);
            }
        }
        while (!mOperators.isEmpty()) {
            popAndEvaluate();
        }
        Double result = mArguments.top();
        mArguments.pop();
        assert mArguments.isEmpty() && mOperators.isEmpty() : "*** Syntax Error ***";
        System.out.println("result at " + x + " is: " + result);
        return result;
    }

}

