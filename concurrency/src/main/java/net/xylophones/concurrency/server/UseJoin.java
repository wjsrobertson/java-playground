package net.xylophones.concurrency.server;

import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

public class UseJoin {

    public static void main(String[] args)  {
        UseJoin use = new UseJoin();

        Runnable runnable = createRunnable(5);
        Thread t1 = new Thread(runnable);
        t1.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    private static Runnable createRunnable(final int durationInSeconds) {
        return new Runnable() {
            @Override
            public void run() {
                final Stopwatch stopWatch = new Stopwatch();
                stopWatch.start();

                while (stopWatch.elapsed(TimeUnit.SECONDS) < durationInSeconds) {
                    System.out.println("Thread '"
                            + Thread.currentThread().getName()
                            + "' - "
                            + stopWatch.elapsed(TimeUnit.SECONDS)
                            + " seconds");

                    try {
                        Thread.sleep(1 * 1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };
    }

}
