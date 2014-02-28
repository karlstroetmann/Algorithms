#include <stdio.h>
#include <stdlib.h>
#include <time.h>

double fibonacci(unsigned n) 
{
	if (n == 0)
		return 0;
	if (n == 1)
		return 1;
	return fibonacci(n - 2) + fibonacci(n - 1);
}

int main() 
{
    unsigned n;        // Eingabe
    double f;          // Ergebnis
    double start;      // Start-Zeit
    double stop;       // Stop-Zeit
    double ticks;      // verbrauchte Rechenzeit
    for (n = 1; n < 50; ++n) {
        start = clock();
        f = fibonacci(n);
        stop = clock();
        ticks = (stop - start) / CLOCKS_PER_SEC * 1000;
        printf( "f(%u) = %g,  Zeit = %g\n", n, f, ticks );
    }
    return (0);
}
