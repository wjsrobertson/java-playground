package net.xylophones.algo.producerconsumer;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    private final BlockingQueue<DataTransferObject> handoverQueue;

    public Producer(BlockingQueue<DataTransferObject> handoverQueue) {
        this.handoverQueue = handoverQueue;
    }

    @Override
    public void run() {
        while (true) {
            DataTransferObject dto = new DataTransferObject("time: " + System.currentTimeMillis());

            try {
                handoverQueue.put(dto);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
