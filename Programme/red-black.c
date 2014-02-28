#include <string>
#include <iostream>

enum Color { BLACK, RED };

class Node
{
private:
	int    _key;
	Node*  _left;
	Node*  _right;
	Node*  _parent;
	Color  _color;

	friend class RedBlackTree;
	
public:
	
	Node(int k, Node* p)
		: _key(k)
		, _left(0)
		, _right(0)
		, _parent(p)
		, _color(RED)
	{}

	bool isLeftChild() 
	{
		return _parent->_left == this;
	}

	Node* uncle()
	{
		Node* g = _parent->_parent;
		return _parent->isLeftChild() ? g->_right : g->_left;
	}		

	void dump()
	{
		cout << _key << ": ";
		cout << (_left   != 0 ? _left  ->_key : 0) << ", ";
		cout << (_right  != 0 ? _right ->_key : 0) << ", ";
		cout << (_parent != 0 ? _parent->_key : 0);
		cout << (_color == BLACK ? " BLACK" : " RED") << endl;
	}
		
};

class RedBlackTree
{
private:
	Node* _root;

	void rotateLeft(Node* u)
	{
		Node* p  = u->_parent;
		Node* w  = u->_right;
		Node* v  = w->_left;
		Color uC = u->_color;
		assert(w->_color == RED);
		u->_right  = v;
		w->_left   = u;
		u->_parent = w;
		w->_parent = p;
		if (v != 0) 
		{	
			v->_parent = u;
		}
		if (p != 0) 
		{
			if (p->_left == u)
			{
				p->_left = w;
			}
			else 
			{
				p->_right = w;
			}
		}
		else
		{
			_root = w;
		}
		w->_color = uC;
		u->_color = RED;
	}
	
	void rotateRight(Node* w)
	{
		Node* p  = w->_parent;
		Node* u  = w->_left;
		Node* v  = u->_right;
		Color wC = w->_color;
		assert(u->_color == RED);
		u->_right  = w;
		w->_left   = v;
		u->_parent = p;
		w->_parent = u;
		if (v != 0 )
		{
			v->_parent = w;
		}
		if (p != 0) 
		{
			if (p->_left == w)
			{
				p->_left = u;
			}
			else 
			{
				p->_right = u;
			}
		}
		else
		{
			_root = u;
		}
		u->_color = wC;
		w->_color = RED;
	}

	void recolor(Node* u)
	{
		assert(u->_left ->_color == RED);
		assert(u->_right->_color == RED);
		u->_left ->_color = BLACK;
		u->_right->_color = BLACK;
		u->_color = RED;
	}

	Node* add(int key)
	{
		if (_root == 0)
		{
			_root = new Node(key, 0);
			return _root;
		}
		Node* n = _root;
		Node* p;
		while (n != 0)
		{
			p = n;
			n = (key < n->_key ? n->_left : n->_right);
		}
		n = new Node(key, p);
		if (key < p->_key)
		{
			p->_left = n;
		}
		else
		{
			p->_right = n;
		}
		return n;
	}
	
	void repair(Node* n) 
	{
		while (n != _root               && 
			   n->_color == RED         && 
			   n->_parent->_color == RED)
		{
			// Case A
			if (n->_parent->isLeftChild())
			{
				if (n->isLeftChild() )
				{
					if (n->uncle() == 0 || n->uncle()->_color == BLACK)
					{
						// Case A.1
						Node* p = n->_parent;
						Node* g = p->_parent;
						rotateRight(g);
						n = p;
					}
					else
					{
						// Case A.4
						Node* g = n->_parent->_parent;
						recolor(g);
						n = g;
					}
				}
				else 
				{
					Node* u = n->uncle();
					// Case A.2
					if (u != 0 && u->_color == RED)
					{
						recolor(n->_parent->_parent);
						n = n->_parent->_parent;						
					}
					else 
					{
						// Case A.3
						Node* g = n->_parent->_parent;
						rotateLeft(n->_parent);
						rotateRight(g);
					}
				}
			}
			else  // Case B
			{
				if (!n->isLeftChild() )
				{
					if (n->uncle() == 0 || n->uncle()->_color == BLACK)
					{
						// Case B.1
						Node* p = n->_parent;
						Node* g = p->_parent;
						rotateLeft(g);
						n = p;
					}
					else
					{
						// Case B.4
						Node* g = n->_parent->_parent;
						recolor(g);
						n = g;
					}
				}
				else 
				{
					Node* u = n->uncle();
					// CASE B.2
					if (u != 0 && u->_color == RED)
					{
						recolor(n->_parent->_parent);
						n = n->_parent->_parent;						
					}
					else 
					{
						// Case B.3
						Node* g = n->_parent->_parent;
						rotateRight(n->_parent);
						rotateLeft(g);
					}
				}
			}
		}
		_root->_color = BLACK;
	}

public:
	RedBlackTree() : _root(0) {}

	void insert(int key)
	{
		Node* n = add(key);
		repair(n);
//		dump(_root);
		cout << endl;
		check(_root);
		checkColor(_root);
	}

	void dump(Node* n)
	{
		if (n == 0)
		{
			return;
		}		
		dump(n->_left);
		n->dump();
		dump(n->_right);
	}

	unsigned check(Node* n)
	{
		if (n == 0)
		{
			return 0;
		}
		if (n->_left != 0)
		{
			assert(n->_left->_parent == n);
		}
		if (n->_right != 0)
		{
			assert(n->_right->_parent == n);
		}		
		unsigned depthLeft  = check(n->_left);
		unsigned depthRight = check(n->_right);
		if (n->_left == 0 || n->_left->_color == BLACK) 
		{
			++depthLeft;
		}
		if (n->_right == 0 || n->_right->_color == BLACK) 
		{
			++depthRight;
		}
		assert(depthLeft == depthRight);
		return depthLeft;
	}

	void checkColor(Node* n)
	{
		if (n == 0)
		{
			return;
		}
		assert(n->_color == BLACK || n->_parent == 0 || n->_parent->_color == BLACK);
	}
};

int main()
{
	RedBlackTree t;
	for (unsigned i = 1000; i > 0; --i)
	{
		int k = static_cast<int>(rand());
		cout << "inserting " << k << endl;
		t.insert(k);
	}
}
