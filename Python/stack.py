class Stack:
    def __init__(self):
        self.mStackElements = []

    def push(self, e):
        self.mStackElements.append(e)

    def pop(self):
        assert len(self.mStackElements) > 0, "popping empty stack"
        self.mStackElements = self.mStackElements[:-1]

    def top(self):
        assert len(self.mStackElements) > 0, "top of empty stack"
        return self.mStackElements[-1]

    def isEmpty(self):
        return self.mStackElements == []

    def copy(self):
        C = Stack()
        C.mStackElements = self.mStackElements[:]
        return C

    def __str__(self):
        C = self.copy()
        result = C._convert()
        return result

    def _convert(self):
        if self.isEmpty():
            return '|'
        t = self.top()
        self.pop()
        return self._convert() + ' ' + str(t) + ' |'

def createStack(L):
    S = Stack()
    n = len(L)
    for i in range(n):
        S.push(L[i])
    return S

if __name__ == '__main__':
    S = createStack(range(10))
    for i in range(10):
        S.pop()
        print(S)
