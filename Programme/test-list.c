#include <stdio.h>
#include <stdlib.h>

typedef unsigned Data;

void printData(Data d) 
{
	printf("%u", d);
};

// int compareData(Data d1, Data d2) 
// {
// 	if (d1 < d2) {
// 		return -1;
// 	} else if (d1 == d2) {
// 		return 0;
// 	} else {
// 		return +1;
// 	}
// }	

int compareData(Data d1, Data d2) {
    if (d1 < d2)            return -1;
        else if (d1 == d2)  return 0;
        else                return +1;    
}   
	
#include "list.c"

int main()
{
	List l = 0;
	for (unsigned i = 0; i < 20; ++i) {
		unsigned number = rand() % 100;
		l = push(number, l);
	}
	printf("The list is:\n");
	printList(l);
	printf("\n");
	printf("Starting to sort now ... \n");
	l = sortList(l);
	printList(l);
	printf("\n");
	assert(isOrdered(l));
	printf("Everything seems to be fine.\n");
	deleteList(l);
	return 0;
}
