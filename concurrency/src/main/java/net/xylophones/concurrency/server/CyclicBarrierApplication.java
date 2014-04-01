package net.xylophones.concurrency.server;

import com.google.common.base.Stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CyclicBarrierApplication {

    public static void main(String[] args) {
        CyclicBarrierApplication app = new CyclicBarrierApplication();
        app.process();
    }

    public void process() {
        Stopwatch stopWatch = new Stopwatch();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Barrier reached! from " + Thread.currentThread().getName());
            }
        };
        CyclicBarrier barrier = new CyclicBarrier(4, myRunnable);

        Callable<Integer> myCallable = createCallable(barrier);

        List<Callable<Integer>> objects = new ArrayList<>();
        objects.add(myCallable);
        objects.add(myCallable);
        objects.add(myCallable);
        objects.add(myCallable);

        stopWatch.start();
        System.out.println("Submitting to executor with invoke ");

        List<Future<Integer>> futures;
        try {
            futures = executorService.invokeAll(objects);
        } catch (InterruptedException e) {
            System.err.println("error: " + e.getMessage());
            return;
        }

        System.out.println("Took " + stopWatch.elapsed(TimeUnit.SECONDS) + "s to submit");
        stopWatch.reset();

        System.out.println("Shutting down");

        executorService.shutdown();
        try {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Waited " + stopWatch.elapsed(TimeUnit.SECONDS) + "s for shutdown");
        stopWatch.reset();

        for (Future<Integer> future : futures) {
            try {
                System.out.println(future.get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                System.err.println("Problem: " + e.getMessage());
            }
        }

        System.out.println("Ended after " + stopWatch.elapsed(TimeUnit.SECONDS) + "s");
    }

    private Callable<Integer> createCallable(final CyclicBarrier barrier) {
        return new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int seconds = 1 + (int) (10 * Math.random());
                System.out.println("Sleeping for " + seconds + " seconds in " + Thread.currentThread().getName());
                Thread.sleep(seconds * 1000);
                barrier.await();
                return null;
            }
        };
    }

}
