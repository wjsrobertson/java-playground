package net.xylophones.algo;

import java.util.concurrent.locks.*;

public class MySynchronousQueue<T> {

    private ReentrantLock lock = new ReentrantLock();

    private Condition listEmpty = lock.newCondition();

    private Condition listFull = lock.newCondition();

    private T contents;

    public void put(T object) throws InterruptedException {
        lock.lock();

        try {
            while (contents != null) {
                listEmpty.await();
            }

            contents = object;
            listFull.signal();
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();

        try {
            while (contents == null) {
                listFull.await();
            }

            T returnObj = contents;
            contents = null;
            listEmpty.signal();
            return returnObj;
        } finally {
            lock.unlock();
        }
    }
}
