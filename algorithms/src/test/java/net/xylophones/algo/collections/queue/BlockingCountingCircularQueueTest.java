package net.xylophones.algo.collections.queue;

import com.google.common.base.Stopwatch;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class BlockingCountingCircularQueueTest {

    BlockingCountingCircularQueue underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new BlockingCountingCircularQueue(2);
    }

    @Test(timeout = 2 * 1000)
    public void checkTakeBlocksWhenEmptyAndRetrievesCorrectElementWhenAvailable() throws Exception {
        Object putObject = new Object();
        Thread addElementThread = new WaitingThread (1, TimeUnit.SECONDS, () -> {
            try {
                underTest.put(putObject);
            } catch (InterruptedException e) {
                return;
            }
        });

        Stopwatch stopWatch = new Stopwatch();
        stopWatch.start();
        addElementThread.start();

        Object retrieved = underTest.take();

        assertThat(stopWatch.elapsed(TimeUnit.MILLISECONDS), greaterThanOrEqualTo(1l * 1000));
        assertEquals(putObject, retrieved);
    }

    @Test(timeout = 2 * 1000)
    public void checkPutBlocksWhenFull() throws Exception {
        underTest.put(new Object());
        underTest.put(new Object());

        Thread removeElementThread = new WaitingThread (1, TimeUnit.SECONDS, () -> {
            try {
                underTest.take();
            } catch (InterruptedException e) {
                return;
            }
        });

        Stopwatch stopWatch = new Stopwatch();
        stopWatch.start();
        removeElementThread.start();

        underTest.put(new Object());

        assertThat(stopWatch.elapsed(TimeUnit.MILLISECONDS), greaterThanOrEqualTo(1l * 1000));
    }
}
