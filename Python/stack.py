#!/usr/bin/env python
# coding: utf-8

# In[1]:


# # Implementing a Stack Class

# First, we define an empty class `Stack`.

# In[2]:


class Stack:
    pass


# Next we define a <em style="color:blue">constructor</em> to this class.  The function $\texttt{stack}(S)$ takes an uninitialized object $S$ and initializes its 
# <em style="color:blue">member variable</em> `mStackElements`.  This member variable is a list containing the data stored in the stack.

# In[3]:


def stack(S):
    S.mStackElements = []


# We add this method to the class Stack.  Since we add it under the name `__init__`, this method will be the constructor.

# In[4]:


Stack.__init__ = stack
del stack


# Next, we add the method `push` to the class `Stack`.  The method $\texttt{push}(S, e)$ pushes $e$ onto the stack $S$.

# In[12]:


def push(S, e):
    S.mStackElements += [e]


# We add this method to the class `Stack`.

# In[13]:


Stack.push = push
del push


# The method `pop` removes the topmost element from a stack.

# In[14]:


def pop(S):
    assert len(S.mStackElements) > 0, "popping empty stack"
    S.mStackElements = S.mStackElements[:-1]

Stack.pop = pop
del pop


# The method `top` returns the element that is on top of the stack.

# In[15]:


def top(S):
    assert len(S.mStackElements) > 0, "top of empty stack"
    return S.mStackElements[-1]

Stack.top = top
del top


# The method `isEmpty` checks whether the stack is empty.

# In[16]:


def isEmpty(S):
    return S.mStackElements == []

Stack.isEmpty = isEmpty
del isEmpty


# The method `copy` creates a shallow copy of the given stack. 

# In[17]:


def copy(S):
    C = Stack()
    C.mStackElements = S.mStackElements[:]
    return C

Stack.copy = copy
del copy


# The method `toStr` converts a stack into a string.

# In[18]:


def toStr(S):
    C = S.copy()
    result = C._convert()
    dashes = "-" * len(result)
    return '\n'.join([dashes, result, dashes])

Stack.__str__ = toStr
del toStr


# The method `convert` converts a stack into a string.

# In[19]:


def convert(S):
    if S.isEmpty():
        return '|'
    top = S.top()
    S.pop()
    return S._convert() + ' ' + str(top) + ' |'

Stack._convert = convert
del convert


def createStack(L):
    S = Stack()
    for x in L:
        S.push(x)
    return S






