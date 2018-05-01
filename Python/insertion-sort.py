import random as rnd

def insertionSort(L):
    "sort the list L"
    if L == []: return []
    return myInsert(L[0], insertionSort(L[1:]))

def myInsert(x, L):
    "insert x into list L"
    if L == []  : return [x]
    if x <= L[0]: return [x] + L
    else        : return [L[0]] + myInsert(x, L[1:])
  
L = [ rnd.randrange(1, 21) for n in range(1, 16)]
print("L = ", L)
S = insertionSort(L);
print("S = ", S)

