#include <stdio.h>
#include <stdlib.h>
#include <time.h>

double fibonacci(unsigned n) 
{
	if (n <= 2) return 1;
	double a, b, c;
	b = 1;
	c = 1;
	for (unsigned i = 2; i < n; ++i) {
		a = b;
		b = c;
		c = a + b;
	}
    return c;
}

int main() 
{
    unsigned n;        // Eingabe
    double f;          // Ergebnis
    double start;      // Start-Zeit
    double stop;       // Stop-Zeit
    double ticks;      // verbrauchte Rechenzeit
    for (n = 1; n < 100; ++n) {
        start = clock();
        f = fibonacci(n);
        stop = clock();
        ticks = (stop - start) / CLOCKS_PER_SEC * 1000;
        printf( "f(%u) = %g,  Zeit = %g\n", n, f, ticks );
    }
    return (0);
}
