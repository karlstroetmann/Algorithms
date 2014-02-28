#include <stdio.h>
#include <stdlib.h>

unsigned sum(unsigned n) {
   if (n == 0)
       return 0;
   return n + sum(n-1);
}

int main() {
  printf("%d", sum(36));
}
