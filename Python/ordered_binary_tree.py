import graphviz as gv

class OrderedBinaryTree:
    def __init__(self):
        self.mKey   = None
        self.mValue = None
        self.mLeft  = None
        self.mRight = None

    def isEmpty(self):
        return self.mKey == None

    def find(self, key):
        if self.isEmpty():
            return None
        elif self.mKey == key:
            return self.mValue
        elif key < self.mKey:
            return self.mLeft.find(key)
        else:
            return self.mRight.find(key)

    def insert(self, key, value):
        if self.isEmpty():
            self.mKey   = key
            self.mValue = value
            self.mLeft  = OrderedBinaryTree()
            self.mRight = OrderedBinaryTree()
        elif self.mKey == key:
            self.mValue = value
        elif key < self.mKey:
            self.mLeft.insert(key, value)
        else:
            self.mRight.insert(key, value)

    def delete(self, key):
        if self.isEmpty():
            return
        if key == self.mKey:
            if self.mLeft.isEmpty():
                self._update(self.mRight)
            elif self.mRight.isEmpty():
                self._update(self.mLeft)
            else:
                rs, km, vm = self.mRight._delMin()
                self.mKey   = km
                self.mValue = vm
                self.mRight = rs
        elif key < self.mKey:
            self.mLeft.delete(key)
        else:
            self.mRight.delete(key)

    def _delMin(self):
        if self.mLeft.isEmpty():
            return (self.mRight, self.mKey, self.mValue)
        else:
            ls, km, vm = self.mLeft._delMin()
            self.mLeft = ls
            return self, km, vm
        
    def toDot(self):
        OrderedBinaryTree.sNodeCount = 0 # static variable of class OrderedBinaryTree
        dot = gv.Digraph(node_attr={'shape': 'record', 'style': 'rounded'})
        NodeDict = {}
        self._assignIDs(NodeDict)
        for n, t in NodeDict.items():
            dot.node(str(n), label='{' + str(t.mKey) + '|' + str(t.mValue) + '}')
        for n, t in NodeDict.items():
            if not t.mLeft.isEmpty():
                dot.edge(str(n), str(t.mLeft.mID))
            if not t.mRight.isEmpty():
                dot.edge(str(n), str(t.mRight.mID))
        return dot

    def _assignIDs(self, NodeDict):
        if self.isEmpty():
            return
        OrderedBinaryTree.sNodeCount += 1
        self.mID = OrderedBinaryTree.sNodeCount
        NodeDict[self.mID] = self
        self.mLeft ._assignIDs(NodeDict)
        self.mRight._assignIDs(NodeDict)


def demo():
    m = OrderedBinaryTree()
    m.insert("anton",   123)
    m.insert("hugo" ,   345)
    m.insert("gustav",  789)
    m.insert("jens",    234)
    m.insert("hubert",  432)
    m.insert("andre",   342)
    m.insert("philipp", 342)
    m.insert("rene",    345)
    return m

t = demo()
d = t.toDot()
d.render('obt')

