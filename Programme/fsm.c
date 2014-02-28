#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

typedef enum { Q0, Q1, Q2, Q3 }   State;
typedef enum { A = 'a', B = 'b' } SIGMA;

bool accept(const char* w) 
{
	 // Beginne im Start-Zustand
	State q = Q0;
	while (*w != 0) {
		switch (q) 
		{
		case Q0: {
			switch (*w) {
				case 'a': {
					q = Q1;
					break;
				}
				case 'b': {
					q = Q2;
					break;
				}
			}
			break;
		}
		case Q1: {
			switch (*w) {
				case 'a': {
					q = Q0;
					break;
				}
				case 'b': {
					q = Q3;
					break;
				}
			}
			break;
		}
		case Q2: {
			switch (*w) {
				case 'a': {
					q = Q3;
					break;
				}
				case 'b': {
					q = Q0;
					break;
				}
			}
			break;
		}
		case Q3: {
			switch (*w) {
				case 'a': {
					q = Q2;
					break;
				}
				case 'b': {
					q = Q1;
					break;
				}
			}
			break;
		}
		}
		++w;
	}
	if (q == Q3)
		return true;
	return false;
}

int main()
{
	while (1) {
		printf("String: ");
		char buffer[20];
		scanf("%s", buffer);
		if (accept(buffer)) {
			printf("%s wurde akzeptiert.\n", buffer);
		} else {
			printf("%s wurde nicht akzeptiert.\n", buffer);
		}
	}
}

	
						
