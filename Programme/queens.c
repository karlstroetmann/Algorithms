#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <sys/types.h>
#include <signal.h>
#include <unistd.h>

// This should have been defined in "signal.h" but unfortunately it isn't.
extern int kill(pid_t pid, int sig);

// The graphical display via LaTeX works only with a BOARD_SIZE of 8.
#define BOARD_SIZE 8

// The process identifier of the xdvi process is stored here.
pid_t xdviPID;

// This gives the numer of seconds that we should wait until the
// next board is displayed.  If numberSeconds is zero, the user has
// to press a key for the next screen to be displayed.
unsigned numberSeconds;

// A board is represented as a struct consisting of an array of unsigned 
// numbers and the number of queens that have already been placed on 
// the board.

typedef struct {
	// The number of queens that have already been placed on the board.
	unsigned numberQueens;  
	// For i = 0 to numberQueens - 1 the number column[i] specifies
	// the column of the queen that has been placed in the i-th row.
	unsigned column[BOARD_SIZE];
} Board;

// Print the given board to the screen.  This is the low tech solution
// that works for BOARD_SIZE different from 8.

// void printBoard(Board* board)
// {
//     printf("Board: \n");
//     for (unsigned i = 0; i < board->numberQueens; ++i) {
//         printf("  %u: %u\n", i, board->column[i]);
//     }
// }


// This function writes the given board as a LaTeX file, translates it, and finally 
// sends a signal to the xdvi process so that the screen is refreshed.

void printBoard(Board* board)
{
	FILE* handle = fopen("chessboard.tex", "w");
	fprintf(handle, "\\documentclass{article}\n");
	fprintf(handle, "\\usepackage{chess}\n");
	fprintf(handle, "\\begin{document}\n");
	fprintf(handle, "\\vspace*{-4cm}\n");	
	fprintf(handle, "\\board{");
	// This variable is true if the next square of the board is black.
	bool color = true;
	for (unsigned i = 0; i < BOARD_SIZE; ++i) {
		if (i > 0) {
			fprintf(handle, "      {");
		}
		for (unsigned j = 0; j < BOARD_SIZE; ++j) {
			if (i < board->numberQueens && j == board->column[i]) {
				fprintf(handle, "Q");
			} else if (color) {
				fprintf(handle, "*");
			} else {
				fprintf(handle, " ");
			}
			color = !color;			
		}
		color = !color;			
		fprintf(handle, "}\n");
	}
	fprintf(handle, "\\showboard\n");
	fprintf(handle, "\\end{document}\n");
	fclose(handle); 
	system("latex chessboard.tex > /dev/null");
	// update the display by sending a signal to xdvi
	kill(xdviPID, SIGUSR1);	
	if (numberSeconds != 0) {
		sleep(numberSeconds);
	} else {
		printf("Press <Enter> for next screen.\n");
		getchar();
	}
}

// Create an empty board.

Board* createEmpty()
{
	Board* newBoard = malloc( sizeof(Board) );
	newBoard->numberQueens = 0;
	return newBoard;
}

// Add a queen to the given board.  The queen is placed in the next free
// row, which is board->numberQueens and this row it is placed in the 
// column that is given a sargument.  A pointer to the new board is returned.

Board* addQueen(Board* board, unsigned nextColumn)
{
	Board* newBoard = malloc( sizeof(Board) );
	newBoard->numberQueens = board->numberQueens + 1;
	// Copy the positions of the queens that have already been placed on
	// the given board.
	for (unsigned i = 0; i < board->numberQueens; ++i) {
		newBoard->column[i] = board->column[i];
	}
	newBoard->column[board->numberQueens] = nextColumn;
	return newBoard;
}

// Check whether it is safe to add a queen in the next free row of the given
// board in the specified column.

bool check(Board* board, unsigned nextColumn) 
{
	// The next free row is given by board->numberQueens.
	unsigned row = board->numberQueens;
	for (unsigned i = 0; i < board->numberQueens; ++i) {
		// Test whether the newly added queen would occupy the same column as 
		// a queen that has already been placed on the board.
		if (board->column[i] == nextColumn) {
			return false;
		}
		// Test whether the newly added queen would occupy the same diagonal as 
		// a queen that has already been placed on the board.
		if (board->column[i] - i == nextColumn - row) {
			return false;
		}
		if (board->column[i] + i == nextColumn + row) {
			return false;
		}
	}
	// If we get this far, it is safe to add the queen.
	return true;
}

// Given a board, try to complete it by filling the remaining rows with queens
// so that no queen can attack another queen.  If this succeeds, a pointer to 
// the new board is returned.  Otherwise, a 0-pointer is returned.

Board* complete(Board* board)
{
	// nothing to do in this case
	if (board->numberQueens == BOARD_SIZE) {
		return board;
	}
	// Check each column of the first empty row whether it is safe to add a new queen.
	for (unsigned i = 0; i < BOARD_SIZE; ++i) {
		if (check(board, i)) {
			Board* nextBoard = addQueen(board, i);
			printBoard(nextBoard);
			// try to add the remaining queens
			Board* result = complete(nextBoard);
			if (result != 0) {
				// Adding the remaining queens has worked, we are done!
				return result;
			} else {
				// We have to backtrack and try the next column.
				printBoard(board);
				free(nextBoard);
				continue;
			}
		}
	}
	// If we ever get here, the given problem is unsolvable.
	return 0;
}

// This programs solves the N-Queens problem: It places N-queens on a 
// NxN chessboard so that the queens cannot attack each other.

// This program accepts one argument, which is a positive natural number. 
// If this argument is given, it specifies the number of secondes that the 
// program should wait until the next screen is displayed.  If no argument
// is given, the program is interactive: the user has to press enter before 
// the next screen is displayed.

int main(int argc, char* argv[])
{	
	if (argc == 2) {
		sscanf(argv[1], "%u", &numberSeconds);
	} else {
		numberSeconds = 0;
	}
	// We fork so that we can start the xdvi viewer in a separate process.
	xdviPID = fork();
	printf("pid = %u\n", xdviPID);
	if (xdviPID == 0) {
		// This is the child process, so we start xdvi here.
		execl("/usr/bin/xdvi", "-s2", "-expert", "chessboard.dvi", NULL);
	} else {
		printf("Press <Enter> to start.\n");
		getchar();
		Board* empty    = createEmpty();
		Board* solution = complete(empty);
		if (empty != 0) {
			printBoard(solution);
		} else {
			printf("Unable to find solution!");
		}
	}
}
