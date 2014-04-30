package net.xylophones.algo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 */
public class ConcurrentLoader {

    private final AtomicLong counter = new AtomicLong();
    private final BlockingQueue<String> queue;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final int numBeforeRequest;
    private final int numToRequest;

    public ConcurrentLoader(int numToRequest, int numBeforeRequest) {
        queue = new ArrayBlockingQueue<>(numToRequest * 2);
        this.numBeforeRequest = numBeforeRequest;
        this.numToRequest = numToRequest;
    }

    public String getInstance() {
        if (counter.incrementAndGet() % numBeforeRequest == numBeforeRequest) {
            executor.submit(() -> {
                queue.addAll(getNewElements(numToRequest));
            });
        }

        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }

    private List<String> getNewElements(int numToGet) {
        return new ArrayList<>();
    }


}
