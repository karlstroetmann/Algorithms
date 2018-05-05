import random as rnd

def selectionSort(L):
    if L == []:
        return []
    x = min(L)
    return [x] + selectionSort(delete(x, L))

def delete(x, L):
    if L == []  : assert False, "element " + str(x) + " not in list " + str(L);
    if x == L[0]: return L[1:]
    else        : return [L[0]] + delete(x, L[1:])

L = [ rnd.randrange(1, 21) for n in range(1, 16) ]
print("L = ", L)
S = selectionSort(L)
print("S = ", S)
