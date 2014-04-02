package net.xylophones.algo.collections.queue;

public class CountingCircularQueue {

    private final Object[] elements;
    private int start = 0;
    private int end = 0;
    private int numElements = 0;

    public CountingCircularQueue(int size) {
        elements = new Object[size];
    }

    private boolean isFull() {
        return (numElements == elements.length);
    }

    private boolean isEmpty() {
        return numElements == 0;
    }

    public void put(Object obj) {
        if (isFull()) {
            throw new IllegalStateException("Queue is full");
        }

        elements[end] = obj;
        end = (end + 1) % elements.length;
        numElements++;
    }

    public Object take() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        Object obj = elements[start];
        start = (start + 1) % elements.length;
        numElements--;
        return obj;
    }

}
