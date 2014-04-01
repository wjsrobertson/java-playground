package net.xylophones.concurrency.server;

import com.google.common.base.Stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConcurrencyApplication {

    public static void main(String[] args) {
        ConcurrencyApplication app = new ConcurrencyApplication();
        app.process();
    }

    public void process() {
        Stopwatch stopWatch = new Stopwatch();
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Callable<Integer> myCallable = createCallable();

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

    private Callable<Integer> createCallable() {
        return new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int seconds = 1 + (int) (10 * Math.random());
                System.out.println("Sleeping for " + seconds + " seconds");
                Thread.sleep(seconds * 1000);
                throw new NumberFormatException();
            }
        };
    }

}
