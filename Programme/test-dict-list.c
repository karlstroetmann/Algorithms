#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

typedef char        Key;
typedef const char* Value;

// Yields true of both keys are equal, 0 otherwise.
unsigned compareKey(Key k1, Key k2) 
{
	if (k1 == k2)
		return true;
	return false;
}

void printKey(Key k) 
{
	printf("'%c'", k);
}

void printVal(Value v) 
{
	printf("\"%s\"", v);
}

#include "list-dictionary.c"

const char * morse_code[40] = {
          ".-",       //  A
          "-...",     //  B
          "-.-.",     //  C
          "-..",      //  D
          ".",        //  E
          "..-.",     //  F
          "--.",      //  G
          "....",     //  H
          "..",       //  I
          ".---",     //  J
          "-.-",      //  K
          ".-..",     //  L
          "--",       //  M
          "-.",       //  N
          "---",      //  O
          ".--.",     //  P
          "--.-",     //  Q
          ".-.",      //  R
          "...",      //  S
          "-",        //  T
          "..-",      //  U
          "...-",     //  V
          ".--",      //  W
          "-..-",     //  X
          "-.--",     //  Y
          "--..",     //  Z
          "-----",    //  0
          ".----",    //  1
          "..---",    //  2
          "...--",    //  3
          "....-",    //  4
          ".....",    //  5
          "-....",    //  6
          "--...",    //  7
          "---..",    //  8
          "----.",    //  9
          ".-.-.-",   //  fullstop, that is "."
          "--..--",   //  comma, that is ","
          "..--.."    //  question mark, that is "?"
};

int main() 
{
	Table t = 0;
	// push lower case letters
	for (unsigned i = 0; i < 26; ++i) {
		char c = 'a' + i;
		t = insert(t, c, morse_code[i]);
	}
	// push upper case letters
	for (unsigned i = 0; i < 26; ++i) {
		char c = 'A' + i;
		t = insert(t, c, morse_code[i]);
	}
	// push digits
	for (unsigned i = 0; i < 10; ++i) {
		char c = '0' + i;
		t = insert(t, c, morse_code[i]);
	}
	t = insert(t, '.', morse_code[36]);
	t = insert(t, ',', morse_code[37]);
	t = insert(t, '?', morse_code[38]);

	printTable(t);
 	char c;
  	while ( (c = getchar()) != EOF ) {
  		Value* valPtr = search(t, c);
  		if (valPtr != 0) {
  			printf("'%c' = \"%s\"\n", c, *valPtr);
  		} else {
  			printf("\n");
  		}
  	}
	t = delete(t, '.');
	t = delete(t, ',');
	t = delete(t, '?');
	printTable(t);
	
}
