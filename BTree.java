/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.pkg2;

/**
 *
 * @author Mahmoud
 */
public class BTree {
    
    private static final int t = 3;

    private Node root;       // root of the B-tree
    private int height;      // height of the B-tree
    private int n;           // number of key-value pairs in the B-tree

    // helper B-tree node data type
    private static final class Node {
        private int numOfChildren;                             // number of children
        private Entry[] children = new Entry[2*t - 1];   // the array of children

        // create a node with k children
        private Node(int k) {
            numOfChildren = k;
        }
    }

    // internal nodes: only use key and next
    // external nodes: only use key and value
    private static class Entry {
        private Integer key;
        private final Object val;
        private Node next;     // helper field to iterate over array entries
        public Entry(Integer key, Object val, Node next) {
            this.key  = key;
            this.val  = val;
            this.next = next;
        }
    }

    /**
     * Initializes an empty B-tree.
     */
    public BTree() {
        root = new Node(0);
    }

    /**
     * Returns the value associated with the given key.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     *         and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public TrackingDevice get(Integer key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        return search(root, key, height);
    }

    private TrackingDevice search(Node x, Integer key, int ht) {
        Entry[] children = x.children;

        if (ht == 0) {
            for (int j = 0; j < x.numOfChildren; j++) {
                if (eq(key, children[j].key)) return (TrackingDevice) children[j].val;
            }
        }

        else {
            for (int j = 0; j < x.numOfChildren; j++) {
                if (eq(key, children[j].key)) return (TrackingDevice) children[j].val;
                
                if (j+1 == x.numOfChildren || key < children[j+1].key)
                    return search(children[j].next, key, ht-1);
            }
        }
        return null;
    }


    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     * @param  val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void add(TrackingDevice val) {
        Node u = insert(root, val, height); 
        n++;
        if (u == null) return;

        // need to split root
        Node t = new Node(2);
        t.children[0] = new Entry(root.children[0].key, null, root);
        t.children[1] = new Entry(u.children[0].key, null, u);
        root = t;
        height++;
    }

    private Node insert(Node h, TrackingDevice val, int ht) {
        int j;
        Entry t = new Entry(val.getId(), val, null);

        // external node
        if (ht == 0) {
            for (j = 0; j < h.numOfChildren; j++) {
                if (less(val.getId(), h.children[j].key)) break;
            }
        }

        // internal node
        else {
            for (j = 0; j < h.numOfChildren; j++) {
                if ((j+1 == h.numOfChildren) || less(val.getId(), h.children[j+1].key)) {
                    Node u = insert(h.children[j++].next, val, ht-1);
                    if (u == null) return null;
                    t.key = u.children[0].key;
                    t.next = u;
                    break;
                }
            }
        }

        for (int i = h.numOfChildren; i > j; i--)
            h.children[i] = h.children[i-1];
        h.children[j] = t;
        h.numOfChildren++;
        if (less(h.numOfChildren, (2*BTree.t - 1))) return null;
        else         return split(h);
    }

    // split node in half
    private Node split(Node h) {
        Node t = new Node((2*BTree.t - 1)/2);
        h.numOfChildren = (2*BTree.t - 1)/2;
        for (int j = 0; j < (2*BTree.t - 1)/2; j++)
            t.children[j] = h.children[(2*BTree.t - 1)/2 + j]; 
        return t;
    }

    // comparison functions - make Comparable instead of Key to avoid casts
    private boolean less(Integer k1, Integer k2) {
        return k1 < k2;
    }

    private boolean eq(Integer k1, Integer k2) {
        return k1.equals(k2);
    }
    
    

}
