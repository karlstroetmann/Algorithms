     int stops(char* p, char* a) {
         char* s = "int loop(char* x) { while (1) {++x;} }";
         int e = equal(s, p, a);
         if (e == 2) {
             return 2;
         } else {
             return 1 - e;
         }
     }
