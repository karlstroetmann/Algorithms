# A list based implementation of quicksort.
def sort(L):
    if L == []:
        return [];
    else:
        pivot = L[len(L) // 2]
        Small = [x for x in L if x < pivot]
        Big   = [x for x in L if x > pivot]
        return sort(Small) + [pivot] + sort(Big)




# code for testing
import random as rnd

def demo():
    L = [ rnd.randrange(1, 200) for n in range(1, 16) ]
    print("L = ", L)
    S = sort(L)
    print("S = ", S)

# n iterations for lists of length k
def testSort(n, k):
    for i in range(1, n+1):
        L = [ rnd.randrange(1, 2*k) for x in range(200) ]
        S = sort(L)
        isOrdered(S)
        sameElements(S, L)
        print('.', end='')
    print()
    print("All tests successful!")


def isOrdered(L):
    for i in range(len(L) - 1):
        assert L[i] <= L[i+1]

def sameElements(L, S):
    assert set(L) == set(S)

demo()
testSort(100, 200)



