package net.xylophones.algo.collections.queue;

public class BlockingCountingCircularQueue {

    private final Object[] elements;
    private int start = 0;
    private int end = 0;
    private int numElements = 0;

    public BlockingCountingCircularQueue(int size) {
        elements = new Object[size];
    }

    private boolean isFull() {
        return (numElements == elements.length);
    }

    private boolean isEmpty() {
        return numElements == 0;
    }

    public synchronized void put(Object obj) throws InterruptedException {
        while (isFull()) {
            wait();
        }

        elements[end] = obj;
        end = (end + 1) % elements.length;
        numElements++;

        notify();
    }

    public synchronized Object take() throws InterruptedException {
        while (isEmpty()) {
            wait();
        }

        Object obj = elements[start];
        elements[start] = null;
        start = (start + 1) % elements.length;
        numElements--;

        notify();

        return obj;
    }

}
