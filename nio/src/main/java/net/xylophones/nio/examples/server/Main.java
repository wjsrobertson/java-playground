package net.xylophones.nio.examples.server;

import net.xylophones.nio.examples.server.WebServerExample;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new WebServerExample().start();

        try {
            Thread.sleep(1000 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
