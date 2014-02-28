/** Edited by Andreas Plattner
    Last modified: 04.06.04 02:19
**/

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <ctype.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>

typedef struct {
    bool isFloat;
    union {
        float number;
        char  symbol;
    } value;
} Token;

// This global variable points to the string that is to be parsed.
char* charPtr;


Token* getNextToken() {
    float result, dec_exp = 0.1; 	//declaration of first decimal place - multiplier
    bool after_dec = false;	
    while ( isspace(*charPtr) )
        ++charPtr;
    if (*charPtr == 0) return 0;
    Token* token = malloc( sizeof(Token) );
    if (isdigit(*charPtr)) {
		result = *charPtr - '0';
        ++charPtr;
        while (isdigit(*charPtr) || *charPtr == '.') { 	//use this loop with float
			if (*charPtr == '.')
				after_dec = true;					//set with proceeding decimal places
			else if (after_dec) {
				result = result + (*charPtr - '0') * dec_exp;	//add up whole-numbers with corresponding decimal place
				dec_exp /= 10;						//advance in decimal place
			} else {
	            result = 10 * result + *charPtr - '0';	//case of whole-number
			}
			++charPtr;
		}
        token->isFloat      = true;
        token->value.number = result;
        return token;
    }
    token->isFloat      = false;
    token->value.symbol = *charPtr;
	const char* sinStr = "sin";
	const char* cosStr = "cos";
    if (strncmp(charPtr, sinStr, 3) == 0 || strncmp(charPtr, cosStr, 3) == 0)
		charPtr +=3;					
    else    
		++charPtr;
    return token;
}

#define dog(x, y) x ## y
#define cat(x, y) dog(x, y)

#define Element float
void cat(print,Element)(Element n) {}
unsigned cat(size,Element)(Element n) {} //sizefloat(float n)
#include "stack-array-macro.c"
#undef Element
#define Element char
void cat(print,Element)(Element n) {}
unsigned cat(size,Element)(Element n) {}
#include "stack-array-macro.c"

unsigned precedenceLevel(char operator) {
    switch (operator) {
        case '+': 
	    case '-': return 1;
        case '*': 
        case '/': 
        case '^': return 3;
	    case 's': 
        case 'c': return 4;		//priority of sin and cos
    }
}

bool isLeftAssociative(char operator) { 			//multiple identical operators
	switch (operator) {
	    case '+': 
	    case '-': 
	    case '*': 
	    case '/': return true;
	    case '^': return false;
	}
}

bool evalBefore(char op1, char op2) {
	if (precedenceLevel(op1) > precedenceLevel(op2)) return true;
	if (precedenceLevel(op1) == precedenceLevel(op2)) 
		return (op1 == op2) ? isLeftAssociative(op1) : true;
	return false;
}

void popCalcPush(
	cat(Stack,float)* argumentStack, 
	cat(Stack,char)*  operatorStack) 
{
    float rhs, lhs, result;
    char operator = cat(top,char)(operatorStack);
    cat(pop,char)(operatorStack);
    if (operator == 's' || operator == 'c') { 	//checks whether sin or cos is operand
		rhs = cat(top,float)(argumentStack); 	//if so only take one argument off stack for unary operator
		cat(pop,float)(argumentStack);
    } else {
    	rhs = cat(top,float)(argumentStack);
		cat(pop,float)(argumentStack);
    	lhs = cat(top,float)(argumentStack);
    	cat(pop,float)(argumentStack);
    }    
    switch (operator) {
        case '+': result = lhs + rhs; break;
        case '-': result = lhs - rhs; break;
        case '*': result = lhs * rhs; break;
        case '/': result = lhs / rhs; break;
        case '^': result = pow(lhs, rhs); break;
	    case 's': result = sin(rhs); break;
  	    case 'c': result = cos(rhs); break;
    }
    cat(push,float)(argumentStack, result);
}

float computeExpr () {
    cat(Stack,float)*   argumentStack = cat(create,float)();
    cat(Stack,char)*  operatorStack = cat(create,char)();
    Token* token = getNextToken();
    while (token) {
        if (token->isFloat) {
            cat(push,float)(argumentStack,token->value.number);
            token = getNextToken();         
            continue;
        } 
        char newOp = token->value.symbol;
        if ( cat(isEmpty,char)(operatorStack) || newOp == '(' ) {
            goto pushOperator;
        }
        char oldOp = cat(top,char)(operatorStack);
        if ( oldOp == '(' && newOp == ')' ) {
            cat(pop,char)(operatorStack);
            token = getNextToken();
            continue;
        } else if ( newOp == ')' ) {
            goto popAndCalculate;
        } else if ( oldOp == '(' ) {
            goto pushOperator;
        } else if ( evalBefore(oldOp, newOp) ) {
            goto popAndCalculate;
        } else {
            goto pushOperator;
        }
    popAndCalculate:
        popCalcPush(argumentStack, operatorStack);
        continue;
    pushOperator:
        cat(push,char)(operatorStack,newOp);
        token = getNextToken();         
    }
    while ( !cat(isEmpty,char)(operatorStack) ) {
        popCalcPush(argumentStack, operatorStack);
    }
    return cat(top,float)(argumentStack);
}

int main () {
    charPtr = malloc( 1001 * sizeof(char) );
    printf( "Arithmetischer Ausdruck: " );
    gets(charPtr);
    printf( "\nErgebnis: %f\n", computeExpr() );



}
