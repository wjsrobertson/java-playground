package net.xylophones.algo.producerconsumer;

import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        BlockingQueue<DataTransferObject> queue = new LinkedBlockingQueue<>();

        Thread producerThread = new Thread(new Producer(queue));
        Thread consumerThread = new Thread(new Consumer(queue));

        producerThread.start();
        consumerThread.start();
    }


}
