package net.xylophones.algo.collections.queue;

public class SimpleCircularQueue {

    private final Object[] elements;
    private int start = 0;
    private int end = 0;

    public SimpleCircularQueue(int size) {
        elements = new Object[size];
    }

    public void put(Object obj) {
        elements[end] = obj;
        end = (end + 1) % elements.length;
    }

    public Object take() {
        Object obj = elements[start];
        start = (start + 1) % elements.length;
        return obj;
    }

}
