package net.xylophones.concurrency.wait;

/**
 * A blocking queue implemented using wait/notify
 */
public class WaitNotifyBlockingQueue {

    private final String[] elements;
    private int currentIndex = 0;

    public WaitNotifyBlockingQueue(int size) {
        elements = new String[size];
    }

    public synchronized void put(String string) throws InterruptedException {
        while (currentIndex >= elements.length) {
            // queue is full - block waiting for space
            wait();
        }

        elements[currentIndex++] = string;
        notifyAll();
    }

    public synchronized String take() throws InterruptedException {
        while (currentIndex <= 0) {
            // queue is empty - block for non-empty
            wait();
        }

        String toReturn = elements[currentIndex];
        elements[currentIndex--] = null;
        notifyAll();

        return toReturn;
    }

}
