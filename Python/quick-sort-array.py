def sort(L):
    "sort the list L"
    quickSort(0, len(L) - 1, L)


def quickSort(a, b, L):
    """
    sort the sublist L[a, b] in place
    a: start index, 
    b: end index
    L: list to sort
    """
    if b <= a:
        return  # at most one element, nothing to do
    m = partition(a, b, L)  # m is the split index
    quickSort(a, m - 1, L)
    quickSort(m + 1, b, L)

def partition(start, end, L):
    """
    Partition the sublist L[start, end] using L[end] as a pivot element.
    The for-loop maintains the following invariants:
    (a) forall i in {start  .. left } : L[i] <= pivot
    (b) forall i in {left+1 .. idx-1} : pivot < L[i]
    This is the algorithm suggested by Nico Lomuto.
    """
    pivot = L[end];
    left  = start - 1;
    for idx in range(start, end):
        if L[idx] <= pivot:
            left += 1
            swap(left, idx, L)
    swap(left + 1, end, L)
    return left + 1

def swap(x, y, L):
    L[x], L[y] = L[y], L[x]

# code for testing
import random as rnd

def demo():
    L = [ rnd.randrange(1, 200) for n in range(1, 16) ]
    print("L = ", L)
    sort(L)
    print("L = ", L)

# n iterations for lists of length k
def testSort(n, k):
    for i in range(1, n+1):
        L = [ rnd.randrange(1, 2*k) for x in range(200) ]
        OldL = L
        sort(L)
        isOrdered(L)
        sameElements(OldL, L)
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


