package net.xylophones.concurrency.wait;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A blocking queue implemented using conditions
 */
public class ConditionBlockingQueue {

    private final String[] elements;
    private int currentIndex = 0;
    private final Lock lock = new ReentrantLock();
    private final Condition notFull;
    private final Condition notEmpty;

    public ConditionBlockingQueue(int size) {
        elements = new String[size];
        notFull = lock.newCondition();
        notEmpty = lock.newCondition();
    }

    public void put(String string) throws InterruptedException {
        try {
            lock.lock();
            while (currentIndex >= elements.length) {
                notFull.await();
            }
            elements[currentIndex++] = string;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public String take() throws InterruptedException {
        try {
            lock.lock();
            while (currentIndex <= 0) {
                notEmpty.await();
            }
            String element = elements[currentIndex];
            elements[currentIndex--] = null;
            notFull.signalAll();
            return element;
        } finally {
            lock.unlock();
        }
    }
}
