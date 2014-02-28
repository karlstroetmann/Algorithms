#include <stdlib.h>
#include <stdio.h>

unsigned fibonacci(unsigned n) 
{
	if (n == 0) {
		return 1;
	} else if (n == 1) {
		return 1;
	} else {
		return fibonacci(n-1) + fibonacci(n-2);
	}
}

int main() 
{
	unsigned i;
	for (i = 0; i < 50; ++i) {
		printf("fib(%d) = %d\n", i, fibonacci(i));
	}
	return 0;
}

	
	
