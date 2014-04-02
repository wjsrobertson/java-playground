package net.xylophones.algo.collections.queue;

import java.util.concurrent.TimeUnit;

/**
 * Thrad that waits for a durtation before running the runnable
 */
public class WaitingThread extends Thread {

    public WaitingThread(long duration, TimeUnit timeUnit, Runnable target) {
        super(
                () -> {
                    try {
                        Thread.sleep(timeUnit.toMillis(duration));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }

                    target.run();
                }
        );
    }
}
