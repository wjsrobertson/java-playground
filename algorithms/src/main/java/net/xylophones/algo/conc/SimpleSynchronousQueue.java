package net.xylophones.algo.conc;

import java.util.concurrent.locks.*;

public class SimpleSynchronousQueue<T> {

    private T contents;
    private ReentrantLock lock = new ReentrantLock();
    private Condition listEmpty = lock.newCondition();
    private Condition listFull = lock.newCondition();

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
