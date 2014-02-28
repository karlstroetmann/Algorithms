#include <stdio.h>
#include <stdlib.h>
#include <time.h>

double fibonacci(unsigned n) 
{
	if (n <= 2) return 1;
	double mem[n];
	// Es gilt später: fib(n) = mem[n - 1]
    mem[0] = 1;  // fib(1) = 1
    mem[1] = 1;  // fib(2) = 1
	for (unsigned i = 0; i < n - 2; ++i)
		mem[i + 2] = mem[i] + mem[i + 1];
    return mem[n - 1];
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
