package net.xylophones.concurrency.server;

public class UseSynchronized {

    public static void main(String[] args)  {

        Object lock = new Object();
        synchronized (lock) {

        }

    }

}
