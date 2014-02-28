#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <ctype.h>
#include <stdbool.h>
#include <assert.h>

typedef struct {
    bool isInt;
    union {
        int  number;
        char symbol;
    } value;
} Token;

char* charPtr;

Token* getNextToken() {
    while ( isspace(*charPtr) )
        ++charPtr;
    if (*charPtr == 0) return 0;
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

#define dog(x, y) x ## y
#define cat(x, y) dog(x, y)

#define Element int
void cat(print,Element)(Element n) {}
unsigned cat(size,Element)(Element n) {}
#include "stack-array-macro.c"
#undef Element
#define Element char
void cat(print,Element)(Element n) {}
unsigned cat(size,Element)(Element n) {}
#include "stack-array-macro.c"

unsigned precedenceLevel(char operator) {
    switch (operator) {
        case '+': case '-':           return 1;
        case '*': case '/': case '%': return 2;
        case '^':                     return 3;
    }
}

bool isLeftAssociative(char operator) {
	switch (operator) {
	    case '+': case '-': case '*': case '/': case '%': return true;
	    case '^':                                         return false;
	}
}

bool evalBefore(char op1, char op2) {
	if (precedenceLevel(op1) > precedenceLevel(op2)) return true;
	if (precedenceLevel(op1) == precedenceLevel(op2)) 
		return (op1 == op2) ? isLeftAssociative(op1) : true;
	return false;
}

void popCalcPush(cat(Stack,int)* argumentStack, 
                 cat(Stack,char)* operatorStack) 
{
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
    }
    cat(push,int)(argumentStack, result);
}

int computeExpr () {
    cat(Stack,int)*   argumentStack = cat(create,int)();
    cat(Stack,char)*  operatorStack = cat(create,char)();
    Token* token = getNextToken();
    while (token) {
        if (token->isInt) {
            cat(push,int)(argumentStack,token->value.number);
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
    return cat(top,int)(argumentStack);
}

int main () {
    charPtr = malloc( 1001 * sizeof(char) );
    gets(charPtr);
    printf( "Ergebnis: %d\n", computeExpr() );
}
