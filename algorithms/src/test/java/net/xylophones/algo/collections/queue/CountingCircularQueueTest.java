package net.xylophones.algo.collections.queue;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CountingCircularQueueTest {

    CountingCircularQueue underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new CountingCircularQueue(2);
    }

    @Test
    public void checkPutAndTakeSingleElementReturnsSameElement() {
        Object element = new Object();

        underTest.put(element);
        Object retrieved = underTest.take();

        assertEquals(element, retrieved);
    }

    @Test(expected = IllegalStateException.class)
    public void checkPutMoreElementsThanQueueLengthFailsWithException() {
        Object element = new Object();

        underTest.put(element);
        underTest.put(element);
        underTest.put(element);
    }

    @Test(expected = IllegalStateException.class)
    public void checkTakeWhenQueueEmptyFailsWithIllegalStateException() {
        underTest.take();
    }
}
