#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

unsigned computeIndex(const char text[], unsigned n) {
	unsigned index = 0;
	for (unsigned i = 0; i < 10; ++i) {
		index = (128 * index + text[i]) % n;
	}
	return index;
}

int main() 
{
	// Annahme: Alle Strings haben höchstens 10 Buchstaben.
	char text[1];
	while (1) {
		printf("String: ");
		scanf("%s", text);
		unsigned idx = computeIndex(text, 22);
		printf("Index(%s) = %u\n", text, idx);
	}
	return 0;
}
