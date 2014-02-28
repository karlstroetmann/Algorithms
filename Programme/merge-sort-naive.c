// An implementation of merge-sort that does not use the malloc() function internally.
// This implementation uses the C99 dialect.  To compile it, use the command
// gcc -std=c99 -Wall -g -o merge-sort merge-sort.c

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <assert.h>

// Create a random Array of the given length.  The array is allocated dynamically.
int* create_random_array(unsigned length) {
    unsigned* result = malloc( length * sizeof(int) );
    for (unsigned i = 0; i < length; ++i)
    {
        result[i] = rand();
    }
    return result;
};

// Return 1 if the array given as argument is ordered.  Otherwise, 0 is returned.
unsigned isSorted(int* array, unsigned length) {
    for (unsigned i = 0; i < length - 1; ++i)
        if (array[i] > array[i+1])
            return 0;
    return 1;
}


// This function merges two array.  The arrays to be merged are part of a bigger array.
// The start address of the bigger array is given in the first parameter, arrayPtr.
// The two smaller arrays are located at
//    arrayPtr[start]  ... arrayPtr[middle - 1]  and
//    arrayPtr[middle] ... arrayPtr[end-1]
// respectively.  Both of these arrays are assumed to be sorted.  The parameter aux
// specifies the start address of an auxiliary array.  This auxiliary array has the same
// size as the array pointed to by arrayPtr.  It is needed by the algorithm as a scratch
// pad memory. On return of the function, the elements in the range
//        arrayPtr[start] ... arrayPtr[end-1] 
// are sorted.
void merge(int arrayPtr[], int start, int middle, int end, int aux[]) 
{    
    for (int i = start; i < end; ++i)
        aux[i] = arrayPtr[i]; 
    // This index runs over the first array.
    int idx1 = start;
    // This index runs over the second array.
    int idx2 = middle;
    // Next we iterate over the elements of the two smaller array and store them
    // in the original array.
    // i runs over the big array.
    int i = start;
    while (idx1 < middle && idx2 < end) 
    {
        if (aux[idx1] < aux[idx2]) {
            arrayPtr[i] = aux[idx1]; 
			++i;
			++idx1;
		} else {
            arrayPtr[i] = aux[idx2]; 
			++i;
			++idx2;
		}
    }
    // If any elements are left in the first array, we copy them to the original array.
    while (idx1 < middle) {
        arrayPtr[i] = aux[idx1];
		++idx1;
		++i;
	}
    // If any elements are left in the second array, we copy them to the original array.
    while (idx2 < end) {
        arrayPtr[i] = aux[idx2];  
		++idx2;
		++i;
	}
}

// This function sorts part of an array.  The start of the array is specified by arrayPtr,
// while the region of the array that needs to be sorted is specified via start and end:
//     arrayPtr[start] ... arrayPtr[end-1]
// is the region of the array that needs to be sorted.  The last variable aux points to
// an array that has the same size as the array that arrayPtr point to.  This array is 
// used as a scratch pad.
//    The algorithm used is a recursive divide-and-conquer strategy.
void merge_sort(int arrayPtr[], int start, int end, int aux[]) {
    // If there are less than two elements, there is nothing to do.
    if (end - start < 2)
        return;
    // Otherwise, we split the portion to be sorted into two parts of approximately the
    // same size, sort these parts recursively, and merge them.
    int middle = (start + end) / 2;                         
    merge_sort( arrayPtr, start, middle, aux );  
    merge_sort( arrayPtr, middle, end, aux );    
    merge( arrayPtr, start, middle, end, aux ); 
}

int main(int argc, char* argv[]) {
    assert(argc == 2);
    unsigned length;
    sscanf(argv[1], "%u", &length);
    int*     ptr     = create_random_array( length );
    double   start   = clock();
    int*     aux     = malloc(length * sizeof(int));
    merge_sort(ptr, 0, length, aux);
    double   stop    = clock();
    double   seconds = (stop - start) / CLOCKS_PER_SEC;
    printf( "Time elapsed: %f seconds.\n", seconds );
    assert( isSorted(ptr, length) );
    return (0);
}
