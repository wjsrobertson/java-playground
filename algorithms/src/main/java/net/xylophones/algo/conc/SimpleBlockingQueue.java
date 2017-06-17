package net.xylophones.algo.conc;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleBlockingQueue<T> {

    private final List<T> contents;
    private final int size;
    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public SimpleBlockingQueue(int size) {
        contents = new ArrayList<T>();
        this.size = size;
    }

    public void put(T obj) throws InterruptedException {
        lock.lockInterruptibly();

        try {
            while (size != contents.size()) {
                notFull.await();
            }

            contents.add(obj);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lockInterruptibly();

        T toReturn;
        try {
            while (contents.size() == 0) {
                notEmpty.await();
            }

            toReturn = contents.remove(contents.size()-1);
            notFull.signal();
            return toReturn;
        } finally {
            lock.unlock();
        }
    }
}
