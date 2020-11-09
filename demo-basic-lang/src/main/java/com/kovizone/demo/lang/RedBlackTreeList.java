package com.kovizone.demo.lang;

/**
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
public class RedBlackTreeList<V extends Comparable<V>> {

    Node<V> root;

    public RedBlackTreeList() {
        super();
        root = null;
    }

    public void add(V arg) {
        if (root == null) {
            root = new Node<>(arg);
        }
    }

    public static class Node<V extends Comparable<V>> {

        Node<V> parentNode;

        Node<V> leftSubNode;

        Node<V> rightSubNode;

        V value;

        public Node(V value) {
            this.parentNode = null;
            this.leftSubNode = null;
            this.rightSubNode = null;
            this.value = value;
        }

        public Node(Node<V> parentNode, V value) {
            this.parentNode = parentNode;
            this.leftSubNode = null;
            this.rightSubNode = null;
            this.value = value;
        }

        public Node<V> getLeftSubNode() {
            return leftSubNode;
        }

        public void setLeftSubNode(Node<V> leftSubNode) {
            this.leftSubNode = leftSubNode;
        }

        public Node<V> getRightSubNode() {
            return rightSubNode;
        }

        public void setRightSubNode(Node<V> rightSubNode) {
            this.rightSubNode = rightSubNode;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}
