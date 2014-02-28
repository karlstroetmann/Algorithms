#include "list-dictionary.c"

typedef struct {
	unsigned noEntries;  // Number of entries in the table.
	unsigned noBuckets;  // The number of buckets, i.e. the size of the array.
	List*    table;      // The array holding pointers to the lists.
} HashTable;

void freeHashTable(HashTable* htPtr);


// A table of prime numbers.  The primes have the form 2^n-k, so they roughly 
// double their size.

unsigned primes[] = { 3,
					  7,
					  13,
	                  31,
	                  61,
                      127,
					  251,
					  509,
					  1021,
					  2039,
					  4093,
					  8191,
					  16381,
					  32749,
					  65521,
					  131071,
					  262139,
					  524287,
					  1048573,
					  2097143,
					  4194301,
					  8388593,
					  16777213,
					  33554393,
					  67108859,
					  134217689,
					  268435399,
					  536870909,
 					  1073741789,
					  2147483647 };

// Construct a hash table of a size which is at most twice as big as the
// given argument and which has at least 13 elements.

HashTable* makeTable(unsigned size) {
	HashTable* ht = malloc( sizeof(HashTable) );
	unsigned index = 0;
	unsigned count = primes[index];
	while (count < size) {
		count = primes[++index];
	}
	ht->noBuckets = count;
	ht->noEntries = 0;
	ht->table = malloc( sizeof(NodePtr) * count );
	for (unsigned i = 0; i < ht->noBuckets; ++i) {
		ht->table[i] = 0;
	}
	return ht;
}

// forward declaration
int hash(const char* key, unsigned size);

// This function creates a table that is about twice as big as the old one.
HashTable* rehash(HashTable* htPtr) 
{
	HashTable* newTable = makeTable(htPtr->noBuckets * 2);
	newTable->noEntries = htPtr->noEntries;
	for (unsigned idx = 0; idx < htPtr->noBuckets; ++idx) {
		NodePtr nodePtr = htPtr->table[idx];
		while (nodePtr != 0) {
			Key      key   = nodePtr->key;
			Value    val   = nodePtr->val;
			unsigned index = hash(key, newTable->noBuckets);
			newTable->table[index] = insert(newTable->table[index], key, val);
			nodePtr = nodePtr->nextPtr;
		}
	}
	freeHashTable(htPtr);
	free(htPtr);
	return newTable;
}


// Deallocate the memory allocated to the given HashTable.
void freeHashTable(HashTable* htPtr)
{
	for (unsigned i = 0; i < htPtr->noBuckets; ++i) {
		freeList(htPtr->table[i]);
	}
	free(htPtr->table);
	free(htPtr);
}

// This hash function works well for strings.  It is the one suggested by
// Aho, Sethi, and Ullmann in their book on compilers.

int hash(const char* key, unsigned size) {
    const    char* ptr;
    unsigned result;
    result = 0;
    ptr    = key;
    while (*ptr != '\0') {
        result = (result << 4) + (*ptr);
        unsigned tmp = result & 0xf0000000;
        if (tmp != 0) {
            result = result ^ (tmp >> 24);
            result = result ^ tmp;
        }
        ptr++;
    }
    return result % size;
}

// search for a given key.

Value* searchTable(HashTable* htPtr, Key key)
{
	unsigned index = hash(key, htPtr->noBuckets);
	return search(htPtr->table[index], key);
}

// Insert a <key, value> Pair into a given hash table.
HashTable* insertTable(HashTable* htPtr, Key key, Value val) 
{
	++(htPtr->noEntries);	
	unsigned index = hash(key, htPtr->noBuckets);
	htPtr->table[index] = insert(htPtr->table[index], key, val);
	// Is load factor greater 4?
	if (htPtr->noEntries > 4 * htPtr->noBuckets) {
		return rehash(htPtr);
	}
	return htPtr;
}

// Delete a given key form the hash table.
void deleteTable(HashTable* htPtr, Key key)
{
	--(htPtr->noEntries);
	unsigned index = hash(key, htPtr->noBuckets);
	htPtr->table[index] = delete(htPtr->table[index], key);
}

// Print the hash table. Used for debugging.

void printHashTable(const HashTable* ht) 
{
	for (unsigned i = 0; i < ht->noBuckets; ++i) {
		printf("| %u | -> ", i);
		if (ht->table[i] != 0) {
			printTable(ht->table[i]);
		} else {
			printf("\n");
		}
	}
	printf("Total number of entries: %u\n", ht->noEntries);
}

