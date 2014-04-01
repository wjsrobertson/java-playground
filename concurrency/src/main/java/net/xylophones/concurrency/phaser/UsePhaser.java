package net.xylophones.concurrency.phaser;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.String.format;

public class UsePhaser {

    public static void main(String[] args) {
        UsePhaser userPhaser = new UsePhaser();
        userPhaser.execute();
    }
    
    private final ExecutorService service = Executors.newFixedThreadPool(
            4, new ThreadFactoryBuilder().setNameFormat("Thread %s").setDaemon(false).build()
    );

    private final Phaser phaser = new Phaser(4) {
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            System.out.println(format("---------- Advancing from phase %s to next phase ------------", getPhase()));
            try {
                Thread.sleep(1000 * 3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return super.onAdvance(phase, registeredParties);
        }
    };

    private final int numIterations = 10;

    public void execute() {
        System.out.println(format("%s registered parties", phaser.getRegisteredParties()));

        for (int j=0 ; j<4 ; j++) {
            service.submit(
                    new Runnable() {
                        @Override
                        public void run() {
                            for (int i=0 ; i<numIterations ; i++) {
                                int waitSeconds = ThreadLocalRandom.current().nextInt(2, 6);
                                String threadName = Thread.currentThread().getName();

                                System.out.println(format("%s performing work for %s seconds", threadName, waitSeconds));


                                try {
                                    Thread.sleep(1000 * waitSeconds);
                                } catch (InterruptedException e) {
                                    System.out.println(format("%s Interrupted - exiting", threadName));
                                    return;
                                }

                                System.out.println(format("%s awaiting others", threadName));
                                int phaseNumber = phaser.arriveAndAwaitAdvance();
                                System.out.println(format("%s arrived - %s", threadName, phaseNumber));
                            }
                        }
                    }
            );
        }
    }

}
