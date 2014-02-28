#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>
#include <math.h>
#include <stdbool.h>
#include <assert.h>

// A simple calculator.

// A token is either a number or an operator symbol.
typedef struct {
	bool isInt;
	union {
		int  number;
		char symbol;
	} value;
} Token;

void printToken(Token* token) 
{
	if (token->isInt) {
		printf("%d", token->value.number);
	} else {
		printf("'%c'", token->value.symbol);
	}
}

// This global variable points to the string that is to be parsed.
char* charPtr;

// Read the next token from the string pointed to charPtr.  This token is 
// returned via the second.  The function returns a pointer to the next 
// character that has not been consumed.
Token* getNextToken()
{
	while ( isspace(*charPtr) ) {
		++charPtr;
	}
	if (*charPtr == 0) {
		return 0;
	}
	Token* token = malloc( sizeof(Token) );
	if (isdigit(*charPtr)) {
		int result = *charPtr - '0';
		++charPtr;
		while (isdigit(*charPtr)) {
			result = 10 * result + *charPtr - '0';
			++charPtr;
		}
		token->isInt        = true;
		token->value.number = result;
		return token;		
	}
	token->isInt        = false;
	token->value.symbol = *charPtr;
	++charPtr;
	return token;
}

#define cataux(x, y) x ## y
#define cat(x, y) cataux(x, y)

#define Element int

void cat(print,Element)(Element n) {
	printf(" %d |", n);
}

unsigned cat(size,Element)(Element n) {
	unsigned size = (n < 0) ? 1 : 0;
	n = (n < 0) ? -n : n;
	if (n == 0) {
		++size;
	}
	while (n > 0) {
		++size;
		n /= 10;
	}
	return size;
}

//#include "stack-array-macro.c"
#include "stack-list.c"

#undef Element
#define Element char

void cat(print,Element)(Element n) {
	printf(" %c |", n);
}

unsigned cat(size,Element)(Element n) {
	return 1;
}

//#include "stack-array-macro.c"
#include "stack-list.c"

// Return the precedence level of an operator.  The predence level is
// greater if the operator binds stronger.
unsigned precedenceLevel(char operator) {
	switch (operator) {
	    case '+': return 1;
	    case '-': return 1;
	    case '*': return 2;
	    case '/': return 2;
	    case '%': return 2;
	    case '^': return 3;
     	default: assert(0);
	}
}

// Return true if the operator is left associative,
bool isLeftAssociative(char operator) {
	switch (operator) {
	    case '+': 
	    case '-': 
	    case '*': 
	    case '/': 
        case '%': return true;
	    case '^': return false;
     	default:  assert(0);
	}
}

// This function returns true iff the operator op1 should be evaluated
// before the operator op2.
bool evalBefore(char op1, char op2) {
	if (precedenceLevel(op1) > precedenceLevel(op2)) {
		return true;
	}
	if (precedenceLevel(op1) == precedenceLevel(op2)) {
		if (op1 == op2) {
			return isLeftAssociative(op1);
		} 
		return true;
	}
	return false;
}

// This function pops an operator from the operator stack, pops the
// corresponding arguments from the argument stack, performs the computation
// associated with the operator, and finally pushes the result onto the argument
// stack.
void popCalcPush(cat(Stack,int)* argumentStack, cat(Stack,char)* operatorStack) {
	char operator = cat(top,char)(operatorStack);
	cat(pop,char)(operatorStack);
	int rhs = cat(top,int)(argumentStack);
	cat(pop,int)(argumentStack);
	int lhs = cat(top,int)(argumentStack);
	cat(pop,int)(argumentStack);
	int result;
	switch (operator) {
	    case '+': result = lhs + rhs; break;
	    case '-': result = lhs - rhs; break;
  	    case '*': result = lhs * rhs; break;
	    case '/': result = lhs / rhs; break;
	    case '%': result = lhs % rhs; break;
	    case '^': result = pow(lhs, rhs); break;
	    default: assert(0);
	}
	cat(push,int)(argumentStack, result);
}

int computeExpr () {
	cat(Stack,int)*   argumentStack = cat(create,int)();
	cat(Stack,char)*  operatorStack = cat(create,char)();
	// Phase 1: process all tokens
	printf("\nStart Phase 1:\n");
	Token* token = getNextToken();
	while (token) {
		cat(printStack,int)(argumentStack);
		cat(printStack,char)(operatorStack);
		printf("\n");
		printf("Last token read: ");
		printToken(token);
		printf("\n");
		// Rule 1: All integers are pushed onto the operand stack.
		if (token->isInt) {
			cat(push,int)(argumentStack,token->value.number);
			token = getNextToken();			
			continue;
		} 
		// At this point, we the token is an operator.
		char newOp = token->value.symbol;
		// Rule 2: If the operator stack is empty or the new operator
		// is '(', the new operator is pushed onto the operator stack.
		if ( cat(isEmpty,char)(operatorStack) || newOp == '(' ) {
			goto pushOperator;
		}
		// At this point we know that the operator stack is not empty.
		char oldOp = cat(top,char)(operatorStack);
		// Rule 3: If the new operator is ')' and the topmost symbol
		// on the operator stack is '(', both parentheses are
		// discarded.
		if ( oldOp == '(' && newOp == ')' ) {
			cat(pop,char)(operatorStack);
			token = getNextToken();
			continue;
		}
		// Rule 4: If the new operator is ')' and the topmost symbol
		// on the operator stack is not '(', we pop the topmost
		// operator from the operator stack, pop its arguments from
		// the operand stack, perform the computation associated with
		// this operator, and finally push the result onto the operand
		// stack.
		if ( newOp == ')' ) {
			assert( oldOp != '(' );
			goto popAndCalculate;
		}
		// Rule 5: If the topmost symbol on the operator stack is '(',
		// the new operator is pushed onto the operator stack.
		if ( oldOp == '(' ) {
			assert( newOp != ')' );
			goto pushOperator;
		}
		// Rule 6: If the operator on top of the operator stack binds
		// at least as strong as the new operator, we pop the topmost
		// operator from the operator stack, pop its arguments from
		// the operand stack, perform the computation associated with
		// this operator, and finally push the result onto the operand
		// stack.
		if ( evalBefore(oldOp, newOp) ) {
			goto popAndCalculate;
		}
		// Rule 7: If the operator on top of the operator stack binds
		// weaker than the new operator, we push the new operator onto
		// the operator stack.
		if ( !evalBefore(oldOp, newOp) ) {
			goto pushOperator;
		}
		assert(0);
	popAndCalculate:
		popCalcPush(argumentStack, operatorStack);
		continue;
	pushOperator:
		cat(push,char)(operatorStack,newOp);
		token = getNextToken();			
	}
	cat(printStack,int)(argumentStack);
	cat(printStack,char)(operatorStack);
	printf("\nStart Phase 2:\n");
	// Phase 2: Pop operators from the operator stack, fetch their
	// arguments from the operand stack, perform the associated
	// computations, and push the results back onto the operand stack
	// until the operator stack is empty.
	while ( !cat(isEmpty,char)(operatorStack) ) {
		popCalcPush(argumentStack, operatorStack);
		cat(printStack,int)(argumentStack);
		cat(printStack,char)(operatorStack);
	}
	return cat(top,int)(argumentStack);
}


int main () {
	printf("Arithmetical Expression: ");
	charPtr = malloc( 1001 * sizeof(char) );
	gets(charPtr);
	int result = computeExpr();
	printf( "Ergebnis: %d\n", result );

	return 0;		
}
