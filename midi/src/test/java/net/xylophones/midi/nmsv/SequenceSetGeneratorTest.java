package net.xylophones.midi.nmsv;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SequenceSetGeneratorTest {

    SequenceSetGenerator underTest = new SequenceSetGenerator();

    @Test
    public void testSequenceOfOne() {
        byte[] test = {0, 1, 2, 3};

        Set<ByteArray> sequenceSets = underTest.createSequenceSets(test, 1);

        assertEquals(4, sequenceSets.size());
        assertTrue(sequenceSets.contains(new ByteArray(new byte[]{0})));
        assertTrue(sequenceSets.contains(new ByteArray(new byte[]{1})));
        assertTrue(sequenceSets.contains(new ByteArray(new byte[]{2})));
        assertTrue(sequenceSets.contains(new ByteArray(new byte[]{3})));
    }

    @Test
    public void testSequenceOfTwo() {
        byte[] test = {0, 1, 2, 3};

        Set<ByteArray> sequenceSets = underTest.createSequenceSets(test, 2);

        assertEquals(3, sequenceSets.size());
        assertTrue(sequenceSets.contains(new ByteArray(new byte[]{0, 1})));
        assertTrue(sequenceSets.contains(new ByteArray(new byte[]{1, 2})));
        assertTrue(sequenceSets.contains(new ByteArray(new byte[]{2, 3})));
    }

    @Test
    public void testSequenceOfThree() {
        byte[] test = {0, 1, 2, 3};

        Set<ByteArray> sequenceSets = underTest.createSequenceSets(test, 3);

        assertEquals(2, sequenceSets.size());
        assertTrue(sequenceSets.contains(new ByteArray(new byte[]{0, 1, 2})));
        assertTrue(sequenceSets.contains(new ByteArray(new byte[]{1, 2, 3})));
    }

}
