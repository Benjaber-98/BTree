/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.pkg2;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Mahmoud
 */
class BTreeNode {
	int[] keys;
	BTreeNode[] child;
	int n;
	boolean leaf;

	// Constructor
	BTreeNode(int t, boolean l) {
		keys = new int[2 * t - 1];
		child = new BTreeNode[2 * t];
		leaf = l;
		n = 0;
	}

	BTreeNode search(int k) {
		int i = 0;
		for (i = 0; i < n; i++) {
			if (keys[i] >= k)
				break;
		}
		if (i < n && keys[i] == k)
			return this;
		if (leaf == true)
			return null;
		//System.out.println(i);
		return child[i].search(k);
	}

	void traverse(int l) {
		for (int i = 0; i < n; i++) {
			if (!leaf)
				child[i].traverse(l + l);
			System.out.print("Level : " + l + " : ");
			System.out.print(keys[i] + " ");
			System.out.println();
		}
		if (!leaf)
			child[n].traverse(l + 1);
	}

	void insertNonFull(int k, int t) {
		if (leaf) {
			int x = n;
			while (x > 0 && keys[x - 1] > k) {
				keys[x] = keys[--x];
			}
			keys[x] = k;
			n++;
		} else {
			int x = 0;
			while (x < n && keys[x] < k)
				x++;
			if (child[x].n == (2 * t - 1))// Child is full
			{
				splitChild(x, this, t);
				int i = x;
				int m = this.child[x].n - 1;
				// this.leaf=false;
				if (this.child[x].keys[m] < k)
					i++;
				this.child[i].insertNonFull(k, t);
				// if(child[x].leaf==true)
				// child[x+1].leaf=true;
			} else {
				child[x].insertNonFull(k, t);
			}
		}
	}

	void splitChild(int ce, BTreeNode y, int t) {
		int mid = (y.child[ce].n / 2);
		int x = n;
		while (x > ce) {
			keys[x] = keys[x - 1];
			x--;
		}
		keys[x] = child[ce].keys[mid];
		int tempChildN = child[ce].n;
		child[ce].n--;
		y.n++;
		BTreeNode nbtn = new BTreeNode(t, true);
		int i = 0;
		// int j = mid;
		while (mid < tempChildN - 1) {
			nbtn.child[i] = child[ce].child[++mid];
			nbtn.keys[i++] = child[ce].keys[mid];
			child[ce].n--;
		}
		nbtn.child[i] = child[ce].child[++mid];
		nbtn.n = i;
		y.child[ce + 1] = nbtn;
		if (child[ce].leaf != false) {
			nbtn.leaf = true;
		} else {
			nbtn.leaf = false;
		}
	}
}

public class BTree {
	int t;
	BTreeNode root;

	BTree(int t) {
		this.t = t;
		root = null;
	}

	void add(int k) {
		// Tree is Empty
		if (root == null) {
			root = new BTreeNode(t, true);
			root.keys[0] = k;
			root.n++;
		}
		// Tree is Not Empty
		else {
			// The root Node is full
			if (root.n == 2 * t - 1) {
				BTreeNode ch = new BTreeNode(t, false);
				ch.child[0] = root;
				root = ch;
				ch.splitChild(0, root, t);
				int i = 0;
				int m = ch.child[0].n - 1;
				if (ch.child[0].keys[m] < k)
					i++;
				ch.child[i].insertNonFull(k, t);
				// ch.child[1].leaf=true;
			} else {
				root.insertNonFull(k, t);
			}
		}
	}
        
        Integer get(int k) {
            BTreeNode n = root.search(k);
            if(n != null) {
                for(int x : n.keys) {
                    if(k == x) return x;
                }
            }
            
            return null;
        }
}
