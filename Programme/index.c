#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

unsigned computeIndex(const char text[])
{
	unsigned result = 0;
	unsigned i = 0;
	while (text[i] != 0) {
		result = 127 * result + text[i];
		++i;
	}
	assert(i < 31);
	return result;
}

int main() 
{
	// Annahme: Alle Strings haben höchstens 30 Buchstaben.
	char text[31];
	while (1) {
		printf("String: ");
		scanf("%s", text);
		unsigned idx = computeIndex(text);
		printf("Index(%s) = %u\n", text, idx);
	}
	return 0;
}
