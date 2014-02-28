#include <stdlib.h>
#include <stdio.h>

double mystery(double x, unsigned n)
{
	if (n == 0)
		return 0;
	double y = mystery(x, n / 2);
	if (n % 2 == 0) {
		return y + y;
	} else {
		return x + y + y;
	}
}

int main()
{
    float    x;
	double   result;
    unsigned n;
	
    while (1)
    {
        printf( "Bitte x angeben: " );
        scanf( "%e", &x );
        printf( "Bitte n angeben: " );
        scanf( "%u", &n );
        if (x == 0)
            return 0;
        result = mystery(x, n);
        printf( "%G = %G * %u\n", result, x, n );
    }
};



