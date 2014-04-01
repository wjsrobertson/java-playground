package net.xylophones.concurrency.cache;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InitialisingCache<T> {

    private final AtomicBoolean cacheInitialised = new AtomicBoolean();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private final ObjectInitialiser<T> initialiser;
    private T cachedObject;

    public InitialisingCache(ObjectInitialiser<T> initialiser) {
        this.initialiser = initialiser;
    }

    public final T get() {
        readLock.lock();

        if (! cacheInitialised.get()) {
            readLock.unlock();
            writeLock.lock();
            try {
                if (! cacheInitialised.get()) {
                    cachedObject = initialiser.create();
                    cacheInitialised.set(true);
                }
            } finally {
                writeLock.unlock();
            }
        } else {
            readLock.unlock();
        }

        return cachedObject;
    }

}
