
# coding: utf-8

# # An Object-Oriented Implementation of the Union-Find Algorithm

# The class `UnionFind` maintains two member variables:
#   - mParent is a dictionary that assigns each node to its parent node.
#     Initially, all nodes point to themselves.
#   - mHeight is a dictionary that stores the height of the tree that a node $x$ is a part of.
#     Initially, all trees contain but a single node and therefore have height $1$.


class UnionFind:
    def __init__(self, M):
        self.mParent = { x: x for x in M }
        self.mHeight = { x: 1 for x in M }
        self.mSize   = { x: 1 for x in M }

# Given an element $x$ from the set $M$, the function $\texttt{self}.\texttt{find}(x)$ 
# returns the ancestor of $x$ that is at the root of the tree containing $x$.


def find(self, x):
    if self.mParent[x] == x:
        return x
    return find(self, self.mParent[x])

UnionFind.find = find


def union(self, x, y):
    root_x = self.find(x)
    root_y = self.find(y)
    if root_x != root_y:
        if self.mHeight[root_x] < self.mHeight[root_y]:
            self.mParent[root_x] = root_y
            self.mSize[root_y] += self.mSize[root_x]
        elif self.mHeight[root_x] > self.mHeight[root_y]:
            self.mParent[root_y] = root_x
            self.mSize[root_x] += self.mSize[root_y]
        else:
            self.mParent[root_y]  = root_x
            self.mHeight[root_x] += 1
            self.mSize[root_x] += self.mSize[root_y]
                
UnionFind.union = union


def partition(M, R):
    UF = UnionFind(M)
    for x, y in R:
        UF.union(x, y)
    Roots = { x for x in M if UF.find(x) == x }
    return [{y for y in M if UF.find(y) == r} for r in Roots]






