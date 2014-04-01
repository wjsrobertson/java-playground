package net.xylophones.algo.producerconsumer;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

    private final BlockingQueue<DataTransferObject> handoverQueue;

    public Consumer(BlockingQueue<DataTransferObject> handoverQueue) {
        this.handoverQueue = handoverQueue;
    }

    @Override
    public void run() {
        while (true) {
            DataTransferObject take = null;

            try {
                take = handoverQueue.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println(take.getName());
        }
    }
}
