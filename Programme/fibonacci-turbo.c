#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>

double fibonacci(unsigned n) 
{
	double w5 = sqrt(5.0);
	double l1 = 0.5 * (1 + w5);
	double l2 = 0.5 * (1 - w5);
	return (pow(l1, n) - pow(l2, n)) / w5;	
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
