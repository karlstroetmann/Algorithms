def sort(L):
    n = len(L);
    if n < 2: return L
    L1, L2 = L[:n//2], L[n//2:]
    return merge(sort(L1), sort(L2))

def merge(L1, L2):
    if L1 == []      : return L2
    if L2 == []      : return L1
    if L1[0] <= L2[0]: return [L1[0]] + merge(L1[1:], L2)
    else             : return [L2[0]] + merge(L1, L2[1:])
























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
