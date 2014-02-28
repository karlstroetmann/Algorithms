#include <stdlib.h>
#include <stdio.h>

unsigned gcd(unsigned a, unsigned b) {
    while (b != 0) {
	int r = a % b;
	a = b;
	b = r;
    }
    return a;
}

int main() {
    for (int i = 2; i < 20; ++i) {
	for (int j = 2; j < 20; ++j) {
	    printf("gcd(%d, %d) = %d\n", i, j, gcd(i,j));
	}
    }
    return 0;
}
