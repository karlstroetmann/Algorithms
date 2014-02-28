#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>


// This is the internal representation of a non-deterministic fsm.  This
// representation assumes that for every state there are at most two 
// successor states.  The item numberStates gives the number of states.
// There are two cases:
//   1.  If the fsm is in state i and symbol[i] is different from 0, then
//       the fsm moves from state i to state next1[i], provided that the 
//       input character is equal to symbol[i].
//   2.  If the fsm is in state i and symbol[i] is equal to 0, then the fsm
//       has an epsilon transition to state next1[i] and to next2[i].
// The start state is always the state with index 0, while the accepting state
// is the last state, i.e. its index is given as numberStates - 1.

typedef struct {
	unsigned  numberStates; // number of states
	char*     symbol;       // array of characters
	unsigned* next1;        // possible next state
	unsigned* next2;        // possible next state
} FSM;

void freeFsm(FSM* fsm);

// Allocate memory for an fsm with n states

FSM* allocateFSM(unsigned n) 
{
	FSM* fsm = malloc( sizeof(FSM) );
	fsm->numberStates = n;
	fsm->symbol       = malloc( n * sizeof(int) );
	fsm->next1        = malloc( n * sizeof(int) );
	fsm->next2        = malloc( n * sizeof(int) );
	return fsm;
}

// Move the FSM f2 into the FSM f1, beginning at position offset.

void move(FSM* f1, FSM* f2, unsigned offset)
{
	for (unsigned i = 0; i < f2->numberStates; ++i) {
		f1->symbol[i + offset] = f2->symbol[i];
		f1-> next1[i + offset] = f2-> next1[i] + offset;
		f1-> next2[i + offset] = f2-> next2[i] + offset;
	}
}

// Enter epsilon transitions taking state i to state j and and to state k
// into the given fsm.

void epsTransition(FSM* fsm, unsigned i, unsigned j, unsigned k) 
{
	fsm->symbol[i] = 0;
	fsm-> next1[i] = j;
	fsm-> next2[i] = k;
}

// Enter a character transitions taking state i to state j on character c
// into the given fsm.

void charTransition(FSM* fsm, unsigned i, unsigned j, char c) 
{
	fsm->symbol[i] = c;
	fsm-> next1[i] = j;
	fsm-> next2[i] = j;
}

// Create an fsm that does not recognize anything.

FSM* createEmptySet()
{
	FSM* fsm = allocateFSM(2);
	epsTransition(fsm, 0, 0, 0);
	epsTransition(fsm, 1, 1, 1);
	return fsm;
}

// Create an fsm that recognizes the empty string.

FSM* createEmptyString()
{
	FSM* fsm = allocateFSM(1);
	epsTransition(fsm, 0, 0, 0);
	return fsm;
}

// Create an fsm that recognizes the character given as argument.

FSM* createCharacter(char c)
{
	FSM* fsm = allocateFSM(2);
	charTransition(fsm, 0, 1, c);
	epsTransition (fsm, 1, 1, 1);
	return fsm;
}

// If f1 is a pointer to an fsm constructed from the regular expression r1 and
// f2  is a pointer to an fsm constructed from the regular expression r2, then
// this procedure constructs an FSM that represents the regular expression r1r2.
// Before the procedure returns, it frees the memory allocated for f1 and f2.

FSM* concat(FSM* f1, FSM* f2)
{
	unsigned n1 = f1 ->numberStates;
	unsigned n2 = f2 ->numberStates;
	unsigned n  = n1 + n2 - 1;
	FSM* fsm = allocateFSM(n);
	move(fsm, f1, 0);
	move(fsm, f2, n1 - 1);
	freeFsm(f1);
	freeFsm(f2);
	return fsm;
}

// If f1 is a pointer to an fsm constructed from the regular expression r1 and
// f2  is a pointer to an fsm constructed from the regular expression r2, then
// this procedure constructs an FSM that represents the regular expression r1 + r2.
// Before the procedure returns, it frees the memory allocated for f1 and f2.

FSM* alternative(FSM* f1, FSM* f2) 
{
	unsigned n1 = f1 ->numberStates;
	unsigned n2 = f2 ->numberStates;
	unsigned n  = n1 + n2 + 2;
	FSM* fsm = allocateFSM(n);
	// new start state pointing to the start states if f1 and f2
	epsTransition(fsm, 0, 1, n1 + 1);
	move(fsm, f1, 1);
	// create an epsilon transition for the last state of f1
	epsTransition(fsm, n1, n - 1, n - 1);
	move(fsm, f2, n1 + 1);
	// create an epsilon transition for the last state of f2
	epsTransition(fsm, n - 2, n - 1, n - 1);
	// let the last state point to itself
	epsTransition(fsm, n - 1, n - 1, n - 1);
	freeFsm(f1);
	freeFsm(f2);
	return fsm;
}

// If f is a pointer to an fsm constructed from the regular expression r, then
// this procedure constructs an FSM that represents the regular expression r1*.
// Before the procedure returns, it frees the memory allocated for f.

FSM* closure(FSM* f) 
{
	unsigned n = f->numberStates;
	FSM* fsm = allocateFSM(n+2);
	// new start state
	epsTransition(fsm, 0, 1, n + 1);
	// copy the first fsm
	move(fsm, f, 1);
	// create epsilon transition from the last state of f
	// to the new final state and to the start state of f
	epsTransition(fsm, n, n + 1, 1);
	// let the last state point to itself
	epsTransition(fsm, n + 1, n + 1, n + 1);
	freeFsm(f);
	return fsm;
}

void printEdge(FILE* handle, unsigned i, unsigned j)
{
	if (i < j) {
		fprintf(handle, "  %u -> %u [label = eps];\n", i, j);
	} else if (i > j) {
		fprintf(handle, "  %u -> %u [label = eps, constraint = false];\n",
				i, j);
	}
}
	
// Create a dot file containing the tree.
void printFsm(FSM* fsm)
{
	FILE* handle = fopen("fsm.dot", "w");
	fprintf(handle, "digraph G {\n");
	fprintf(handle, "  rankdir = LR;\n");
	fprintf(handle, "  Start->0;\n");
	fprintf(handle, "  Start [ shape = plaintext ];\n");
	unsigned n = fsm->numberStates;
	for (unsigned i = 0; i < n; ++i) {
		if (fsm->symbol[i] == 0) {
			printEdge(handle, i, fsm->next1[i]);
			if (fsm->next1[i] != fsm->next2[i]) {
				printEdge(handle, i, fsm->next2[i]);
			}
		} else {
			fprintf(handle, "  %u -> %u [label = %c];\n", 
					i, fsm->next1[i], fsm->symbol[i]);
		}
	}
	fprintf(handle, "  %u [ peripheries = 2 ];\n", n - 1);
	fprintf(handle, "}\n");
	fclose(handle);
	system("dot -Tps fsm.dot -o fsm.ps; gv fsm.ps &");
}

// Construct one specific FSM.  It is the fsm that results from converting
// the regular expression (a|b)*abb into a non-deterministic fsm.

FSM* constructFSM() 
{
	FSM* fsm = malloc( sizeof(FSM) );
	fsm->numberStates = 11;
	fsm->symbol       = malloc( fsm->numberStates * sizeof(int) );
	fsm->next1        = malloc( fsm->numberStates * sizeof(int) );
	fsm->next2        = malloc( fsm->numberStates * sizeof(int) );

	fsm->symbol[0] = 0;
	fsm->symbol[1] = 0;
	fsm->symbol[2] = 'a';
	fsm->symbol[3] = 0;
	fsm->symbol[4] = 'b';
	fsm->symbol[5] = 0;
	fsm->symbol[6] = 0;
	fsm->symbol[7] = 'a';
	fsm->symbol[8] = 'b';
	fsm->symbol[9] = 'b';
	fsm->symbol[10] = 0;

	fsm->next1[0] = 1;
	fsm->next1[1] = 2;
	fsm->next1[2] = 3;
	fsm->next1[3] = 6;
	fsm->next1[4] = 5;
	fsm->next1[5] = 6;
	fsm->next1[6] = 7;
	fsm->next1[7] = 8;
	fsm->next1[8] = 9;
	fsm->next1[9] = 10;
	fsm->next1[10] = 10;
	
	fsm->next2[0] = 7;
	fsm->next2[1] = 4;
	fsm->next2[2] = 3;
	fsm->next2[3] = 6;
	fsm->next2[4] = 5;
	fsm->next2[5] = 6;
	fsm->next2[6] = 1;
	fsm->next2[7] = 8;
	fsm->next2[8] = 9;
	fsm->next2[9] = 10;
	fsm->next2[10] = 10;
	return fsm;
}

void freeFsm(FSM* fsm) 
{
	free(fsm->symbol);
	free(fsm->next1);
	free(fsm->next2);
	free(fsm);
}

// Close the current set of states under epsilon transitions.

void epsClose(FSM* fsm, bool states[])
{
	bool change = true;
	while (change) {
		change = false;
		for (unsigned i = 0; i < fsm->numberStates; ++i) {
			if (states[i] && fsm->symbol[i] == 0) {
				if (!states[fsm->next1[i]]) {
					change = true;
					states[fsm->next1[i]] = true;
				}
				if (!states[fsm->next2[i]]) {
					change = true;
					states[fsm->next2[i]] = true;
				}
			}
		}
	}
}

// Given an fsm and a word, this fsm checks, whether the given fsm accepts the
// given word.  In this case, true is returned, else the result is false.

bool simulate(FSM* fsm, char* word)
{
	// This array describes the set of states that the fsm can be in.
    // If the fsm might be in state number i, then currentStates[i] = true.
	bool currentStates[fsm->numberStates];
	// The set of states that the fsm might be in, after the current character
	// has been consumed.
	bool nextStates[fsm->numberStates];
	currentStates[0] = true;
	for (unsigned i = 1; i < fsm->numberStates; ++i) {
		currentStates[i] = false;
	}
	epsClose(fsm, currentStates);
	while (*word != 0) {
		for (unsigned i = 0; i < fsm->numberStates; ++i) {
			nextStates[i] = false;
		}
		for (unsigned i = 0; i < fsm->numberStates; ++i) {
			if (currentStates[i] && fsm->symbol[i] == *word) {
				nextStates[fsm->next1[i]] = true;
			}
		}
		for (unsigned i = 0; i < fsm->numberStates; ++i) {
			currentStates[i] = nextStates[i];
		}
		epsClose(fsm, currentStates);
		++word;
	}
	return currentStates[fsm->numberStates - 1];
}


int main()
{
	FSM* fsm = concat(closure(alternative(createCharacter('a'), 
										  createCharacter('b'))),
					  concat(createCharacter('a'), 
							 concat(createCharacter('b'), createCharacter('b'))));
	printFsm(fsm);
	char buffer[100];
	do {
		printf("String: ");
		scanf("%s", buffer);
		if (simulate(fsm, buffer)) {
			printf("%s wurde akzeptiert.\n", buffer);
		} else {
			printf("%s wurde nicht akzeptiert.\n", buffer);
		}
	} while (buffer[1]);
	freeFsm(fsm);
}
