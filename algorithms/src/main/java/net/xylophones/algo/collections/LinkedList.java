package net.xylophones.algo.collections;

/**
 * A simple doubly linked list
 */
public class LinkedList<T> {

    private Node topNode;

    private Node tailNode;

    public void add(T entry) {
        Node newNode = new Node();
        newNode.value = entry;

        if (tailNode != null) {
            newNode.previous = tailNode;
            tailNode.next = newNode;
            tailNode = newNode;
        } else {
            topNode = newNode;
            tailNode = newNode;
        }
    }

    public void remove(int index) {
        Node removeNode = getNode(index);

        if (removeNode == topNode) {
            topNode = removeNode.next;
        }
        if (removeNode == tailNode) {
            tailNode = removeNode.previous;
        }
        if (removeNode.previous != null) {
            removeNode.previous.next = removeNode.next;
        }
    }

    public T get(int index) {
        return getNode(index).value;
    }

    private Node getNode(int index) {
        Node currentNode = topNode;

        for (int i=0 ; i<=index  ; i++) {
            if (currentNode == null) {
                break;
            }
            if (i == index) {
                return currentNode;
            }

            currentNode = currentNode.next;
        }

        throw new IndexOutOfBoundsException();
    }

    private class Node {
        T value;
        Node previous;
        Node next;
    }

}
