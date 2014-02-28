#include "stdio.h"
#include "stdlib.h"
#include "string.h"
#include "stdbool.h"

typedef char*    Key;
typedef unsigned Value;

// Compare two strings.  This function is needed in the file
// "list-dictionary.c" that is included below.

bool compareKey(const char* s1, const char* s2) 
{
	if (strcmp(s1, s2) == 0) 
		return true;
	return false;
}

void printKey(const char* s) 
{
	printf("\"%s\"", s);
}

void printVal(unsigned u) 
{
	printf("%u", u);
}

#include "hash-dict-naive.c"
// #include "hash-dict.c"

char* namen[] = {
	"Alexander",
	"Benedikt",
	"Christoph",
	"Michael",
	"Elisabet",
	"Frederic",
	"Steffen",
	"Christoph",
	"Nicola",
};

// Read one line from the input, allocate memory for it and return
// a pointer to the allocated memory.
char* getline() 
{
	char buffer[256];
	do {
		gets(buffer);
	} while (buffer[0] == 0);
	unsigned length = strlen(buffer);
	char* line = malloc(length + 1);
	strcpy(line, buffer);
	return line;
}

void interactive()
{
	HashTable* ht = makeTable(4);
	for (unsigned i = 0; i < sizeof(namen)/sizeof(char*); ++i) {
		ht = insertTable(ht, namen[i], rand() % 100);
	}
	printHashTable(ht);
	while (1) {
		unsigned i;
		printf( "Insert (2) or search (1) or delete (0)? " );
		scanf(  "%u", &i );
		if (i == 2) {
			printf( "Name: " );
			char* name = getline();
			unsigned telNr;
			printf( "Telephone-Nr.: " );
			scanf("%u", &telNr);
			ht = insertTable(ht, name, telNr);
			printHashTable(ht);
		} else if (i == 1) {
			printf( "Name: " );
			char* name = getline();
			unsigned* telNr = searchTable(ht, name);
			printf( "Telephone-Nr.: %u\n", *telNr );
		} else {
			printf( "Name: " );
			char* name = getline();
			deleteTable(ht, name);
			printHashTable(ht);
		}
	}
}

int main()
{
	interactive();
	return 0;
}
