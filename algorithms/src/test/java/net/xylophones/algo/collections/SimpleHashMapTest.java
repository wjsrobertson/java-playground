package net.xylophones.algo.collections;

import org.junit.Before;
import org.junit.Test;

import javax.sound.midi.VoiceStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimpleHashMapTest {

    SimpleHashMap<Integer,Integer> underTest;

    @Before
    public void setUp() {
        underTest = new SimpleHashMap<Integer,Integer>(10);
    }

    @Test
    public void testAddAndCheckOneValue() {
        underTest.put(10, 20);
        assertEquals(20, underTest.get(10).intValue());
    }

    @Test
    public void testAddAndRemoveOneValue() {
        underTest.put(10, 20);
        underTest.remove(10);
        assertNull(underTest.get(10));
    }

}
