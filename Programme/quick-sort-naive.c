// An implementation of merge-sort that does not use the malloc() function internally.
// This implementation uses the C99 dialect.  To compile it, use the command
// gcc -std=c99 -Wall -g -o merge-sort merge-sort.c

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <stdbool.h>
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

void print_array(int a[], int start, int length) {
	for (int i = start; i < length; ++i) {
		if (i+1 < length) {
			printf("%d ",  a[i]);
		} else {
			printf("%d\n", a[i]);
		}
	}
}


// Exchange array[i] and array[j].
void swap(int* array, int i, int j) {
	if (i == j) 
		return;
	unsigned temp = array[i];
	array[i] = array[j];
	array[j] = temp;	
}

// This function defines x := array[start] and partitions the array given as
// first argument into two parts.  The first part contains all elements that
// are less or equal than x, the second part contains the remaining elements.
// The arguments start and end specify the part of the array that is to be
// partitioned.  The partitioning is done in place.  The function returns the
// index i which the element x takes in the new array.  Formally, when the
// function terminates with result i, the following is satisfied:
//
//    1.  forall j in {start..i-1} : array[j] <= x
//    2.  forall j in {i+1..end}   : x < array[j]
//    3.  array[i] = x.
//
int partition(int* array, int start, int end) {
    int x        = array[start];
    int leftIdx  = start + 1;
    int rightIdx = end;
    while (true) {
        while (leftIdx  <= end   && array[leftIdx]  <= x) ++leftIdx;
        while (rightIdx >  start && array[rightIdx] >  x) --rightIdx;
        if (leftIdx >= rightIdx) break;
        swap(array, leftIdx, rightIdx);
    }
    swap(array, start, rightIdx);
    return rightIdx;    
}

// This function sorts part of an array.  The start of the array is specified by arrayPtr,
// while the region of the array that needs to be sorted is specified via start and end:
//     arrayPtr[start] ... arrayPtr[end]
// is the region of the array that needs to be sorted.  
void quick_sort(int* array, int start, int end) {
    // If there are less than two elements, there is nothing to do.
    if (end - start < 1) return;
    int splitIdx = partition(array, start, end);
    quick_sort( array, start, splitIdx - 1);  
    quick_sort( array, splitIdx + 1, end );    
}

int main(int argc, char* argv[]) {
    assert(argc == 2);
    unsigned length;
    sscanf(argv[1], "%u", &length);
    int*     ptr     = create_random_array( length + 1 );
//	print_array(ptr, 0, length);
    double   start   = clock();
    quick_sort(ptr, 0, length);
    double   stop    = clock();
    double   seconds = (stop - start) / CLOCKS_PER_SEC;
    printf( "Time elapsed: %f seconds.\n", seconds );
//	print_array(ptr, 0, length);
    assert( isSorted(ptr, length + 1) );
    return (0);
}
