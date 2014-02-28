//#include <malloc.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
//#include <unistd.h>
#include <assert.h>

// defined externally
void sound(const char* c);
int  usleep(unsigned long time);

typedef struct Node* NodePtr;

struct Node {
    char          key;
    const char*   code;
    NodePtr       nextPtr;
};

// A list is just a pointer to its first node.
typedef NodePtr List;

// A constructor for list nodes.
NodePtr createNode(char c, const char* code, NodePtr next)
{
    NodePtr nodePtr  = malloc( sizeof(struct Node) );
    nodePtr->key     = c;
    nodePtr->code    = code;
    nodePtr->nextPtr = next;
    return nodePtr;
}

// add an element at the front of the list
List push(char c, const char* code, List l)
{
    return createNode(c, code, l);
}

// This destructor destroys a list and frees the associated nodes.
void deleteList(List l)
{
    if (l == 0)
        return;
    List rest = l->nextPtr;
    free(l);
    deleteList(rest);
};

void printList(List l)
{
    if (l == 0) {
        printf("\n");
        return;
    }
    printf("'%c' = \"%s\"\n", l->key, l->code);
    printList(l->nextPtr);
};

const char* search(char c, List l)
{
    if (l == 0) {
        return 0;
    }
    if (l->key == c) {
        return l->code;
    } else {
        return search(c, l->nextPtr);
    }
}

List insert(NodePtr n, List l)
{
    if (l == 0) {
        n->nextPtr = 0;
        return n;
    }
    if (n->key <= l->key) {
        n->nextPtr = l;
        return n;
    } else {
        NodePtr newNext = insert(n, l->nextPtr);
        l->nextPtr = newNext;
        return l;
    }
}

List sortList(List l)
{
    if (l == 0)
        return l;
    NodePtr next   = l->nextPtr;
    NodePtr sorted = sortList(next);
    return insert(l, sorted);
}

bool isOrdered(List l)
{
    if (l == 0)
        return true;
    if (l->nextPtr == 0)
        return true;
    char c1 = l->key;
    char c2 = l->nextPtr->key;
    if (c1 <= c2) {
        return isOrdered(l->nextPtr);
    } else {
        return false;
    }
}

//  Below is the Morse Code:
//  A          .-
//  B          -...
//  C          -.-.
//  D          -..
//  E          .
//  F          ..-.
//  G          --.
//  H          ....
//  I          ..
//  J          .---
//  K          -.-
//  L          .-..
//  M          --
//  N          -.
//  O          ---
//  P          .--.
//  Q          --.-
//  R          .-.
//  S          ...
//  T          -
//  U          ..-
//  V          ...-
//  W          .--
//  X          -..-
//  Y          -.--
//  Z          --..
//  0          -----
//  1          .----
//  2          ..---
//  3          ...--
//  4          ....-
//  5          .....
//  6          -....
//  7          --...
//  8          ---..
//  9          ----.
//  Fullstop   .-.-.-
//  Comma      --..--
//  Query      ..--.. 

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
    List l = 0;
    // push lower case letters
    for (unsigned i = 0; i < 26; ++i) {
        char c = 'a' + i;
        l = push(c, morse_code[i], l);
    }
    // push upper case letters
    for (unsigned i = 0; i < 26; ++i) {
        char c = 'A' + i;
        l = push(c, morse_code[i], l);
    }
    // push digits
    for (unsigned i = 0; i < 10; ++i) {
        char c = '0' + i;
        l = push(c, morse_code[i + 26], l);
    }
    l = push('.', morse_code[36], l);
    l = push(',', morse_code[37], l);
    l = push('?', morse_code[38], l);

     char c;
     while ( (c = getchar()) != EOF ) {
         const char* code = search(c, l);
         if (code != 0) {
             printf("%s\n", code);
             sound(code);
         } else {
             printf("\n");
             usleep(150000);
         }
         usleep(350000);
     }

//     const char* sos = "sos";
//     while ( (c = *sos) != 0 ) {
//         const char* code = search(c, l);
//         sound(code);
//         ++sos;
//     }
    
    return 0;
}




    
