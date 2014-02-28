#include <string.h>
#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

typedef struct Sum*    SumPtr;
typedef struct Prod*   ProdPtr;
typedef struct Factor* FactorPtr;

unsigned nodeCounter = 0;

// If arg1 != 0, then this structure represents the sum or difference of the
// expression represented by arg1 and the expression represented by arg2,
// else it represents the expression that is represented by arg2.

struct Sum {
	char     operation;  // either "+" or "-"
	SumPtr   arg1;
	ProdPtr  arg2;
	unsigned ctr;  // used only for printing	
};

// If arg1 != 0, then this structure represents the product or quotient
// of the expression represented by arg1 and the expression represented
// by arg2, else it represents the expression that is represented by arg2.

struct Prod {
	char      operation;  // either "*" or "/"
	ProdPtr   arg1;
	FactorPtr arg2;
	unsigned ctr;  // used only for printing
};


// If sumPtr == 0, then number is valid, else sumPtr is valid.

struct Factor {
	int      number;
	SumPtr   sumPtr;
	unsigned ctr;  // used only for printing
};

int evalSum(SumPtr sum);

int evalFactor(FactorPtr factor) 
{
	if (factor->sumPtr == 0) {
		return factor->number;
	} else {
		return evalSum(factor->sumPtr);
	}
}

int evalProd(ProdPtr prod) 
{
	if (prod->arg1 == 0) {
		return evalFactor(prod->arg2);
	} else if (prod->operation == '*') {
		return evalProd(prod->arg1) * evalFactor(prod->arg2);
	} else {
		return evalProd(prod->arg1) / evalFactor(prod->arg2);
	}
}

int evalSum(SumPtr sum) 
{
	if (sum->arg1 == 0) {
		return evalProd(sum->arg2);
	} else if (sum->operation == '+') {
		return evalSum(sum->arg1) + evalProd(sum->arg2);
	} else {
		return evalSum(sum->arg1) - evalProd(sum->arg2);
	}
}

unsigned findCtrSum(SumPtr sum);

unsigned findCtrFactor(FactorPtr factor) 
{
	if (factor->sumPtr == 0) {
		return factor->ctr;
	} else {
		return findCtrSum(factor->sumPtr);
	}
}

unsigned findCtrProd(ProdPtr prod) 
{
	if (prod->arg1 == 0) {
		return findCtrFactor(prod->arg2);
	} else {
		return prod->ctr;
	}
}

unsigned findCtrSum(SumPtr sum) 
{
	if (sum->arg1 == 0) {
		return findCtrProd(sum->arg2);
	} else {
		return sum->ctr;
	}
}

void printSum(FILE* handle, SumPtr sum);

void printFactor(FILE* handle, FactorPtr factor) 
{
	if (factor->sumPtr == 0) {
		fprintf(handle, "%u [label = \"%d\"]\n", factor->ctr, factor->number);
	} else {
		printSum(handle, factor->sumPtr);
	}
}

void printProd(FILE* handle, ProdPtr prod) 
{
	if (prod->arg1 == 0) {
		printFactor(handle, prod->arg2);
	} else {
		fprintf(handle, "%u -> %u;\n", prod->ctr, findCtrProd(prod->arg1));
		fprintf(handle, "%u -> %u;\n", prod->ctr, findCtrFactor(prod->arg2));
		fprintf(handle, "%u [label = \"%c\"]\n", prod->ctr, prod->operation);
		printProd(handle,   prod->arg1);
		printFactor(handle, prod->arg2);
	}
}

void printSum(FILE* handle, SumPtr sum) 
{
	if (sum->arg1 == 0) {
		printProd(handle, sum->arg2);
	} else {
		fprintf(handle, "%u -> %u;\n", sum->ctr, findCtrSum(sum->arg1));
		fprintf(handle, "%u -> %u;\n", sum->ctr, findCtrProd(sum->arg2));
		fprintf(handle, "%u [label = \"%c\"]\n", sum->ctr, sum->operation);
		printSum(handle,  sum->arg1);
		printProd(handle, sum->arg2);
	}
}

void printGraph(SumPtr sum)
{
	FILE* handle = fopen("sum.dot", "w");
	fprintf(handle, "digraph G {\n");
	printSum(handle, sum);
	fprintf(handle, "}\n");
	fclose(handle);
	system("dot -Tps sum.dot -o sum.ps; gv sum.ps &");
}


// A global variable is used here since passing a pointer by reference 
// would require pointers to pointers and that gets akward.

char* charPtr;

FactorPtr parseFactor(char* begin, char* end);
ProdPtr   parseProd(char* begin, char* end);

SumPtr parseSum(char* begin, char* end)
{
	ProdPtr prod = parseProd(begin, end);
	if (prod != 0) {
		SumPtr sum = malloc( sizeof(struct Sum) );
		sum->arg1 = 0;
		sum->arg2 = prod;
		sum->ctr  = nodeCounter++;
		return sum;
	}
	// Scan for "+" and "-"
	for (char* charPtr = begin + 1; charPtr < end; ++charPtr) {
		if (*charPtr == '+' || *charPtr == '-') {
			SumPtr firstSum = parseSum(begin, charPtr);
			if (firstSum != 0) {
				ProdPtr prod = parseProd(charPtr + 1, end);
				if (prod != 0) {
					SumPtr sum = malloc( sizeof(struct Sum) );
					sum->operation = *charPtr;
					sum->arg1 = firstSum;
					sum->arg2 = prod;
					sum->ctr  = nodeCounter++;
					return sum;
				} else {
					free(firstSum);
				}
			}
		}
	}
	return 0;
}

ProdPtr parseProd(char* begin, char* end)
{
	FactorPtr factor = parseFactor(begin, end);
	if (factor != 0) {
		ProdPtr prod = malloc( sizeof(struct Prod) );
		prod->arg1 = 0;
		prod->arg2 = factor;
		prod->ctr = nodeCounter++;
		return prod;
	}
	// Scan for "*" and "/"
	for (char* charPtr = begin + 1; charPtr < end; ++charPtr) {
		if (*charPtr == '*' || *charPtr == '/') {
			ProdPtr firstProd = parseProd(begin, charPtr);
			if (firstProd != 0) {
				FactorPtr factor = parseFactor(charPtr + 1, end);
				if (factor != 0) {
					ProdPtr prod = malloc( sizeof(struct Prod) );
					prod->operation = *charPtr;
					prod->arg1 = firstProd;
					prod->arg2 = factor;
					prod->ctr = nodeCounter++;
					return prod;
				} else {
					free(firstProd);
				}
			}
		}
	}
	return 0;
}

FactorPtr parseFactor(char* begin, char* end)
{
	if (*begin == '(') {
		SumPtr sum = parseSum(begin + 1, end - 1);
		if (sum != 0 && *(end-1) == ')') {
			FactorPtr factor = malloc( sizeof(struct Factor) );
			factor->sumPtr = sum;
			factor->ctr    = nodeCounter++;
			return factor;
		} else {
			free(sum);
			return 0;
		}
	} 
	int sign = 1;
	if (*begin == '+') {
		++begin;
	} else if (*begin == '-') {
		++begin;
		sign = -1;
	}	
	if (isdigit(*begin)) {
		int num = 0;
		while (isdigit(*begin) && begin < end) {
			num = 10 * num + *begin - '0';
			++begin;
		}
		if (begin == end) {
			FactorPtr factor = malloc( sizeof(struct Factor) );
			factor->number = sign * num;
			factor->sumPtr = 0;
			factor->ctr    = nodeCounter++;
			return factor;
		} 
	}
	return 0;
}

int main()
{
	char* buffer = malloc( 1001 * sizeof(char) );
	printf("Arithmetical Expression: ");
	scanf("%s", buffer);
	unsigned length = strlen(buffer);
	SumPtr sum = parseSum(buffer, buffer + length);
	printGraph(sum);
	unsigned result = evalSum(sum);
	printf("%s = %d\n", buffer, result);
}
