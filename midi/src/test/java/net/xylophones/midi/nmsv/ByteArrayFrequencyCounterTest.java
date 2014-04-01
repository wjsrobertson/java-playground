package net.xylophones.midi.nmsv;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ByteArrayFrequencyCounterTest {

    ByteArrayFrequencyCounter underTest = new ByteArrayFrequencyCounter();

    @Test
    public void testMatchAtStart() throws Exception {
        byte[] set = {1, 2, 3, 4, 5};
        ByteArray toTest = new ByteArray(new byte[]{1, 2});

        MatchInformation matchInformation = underTest.checkForMatches(toTest, set);
        assertEquals(1, matchInformation.numMatches);
    }

    @Test
    public void testMatchAtEnd() throws Exception {
        byte[] set = {1, 2, 3, 4, 5};
        ByteArray toTest = new ByteArray(new byte[]{4, 5});

        MatchInformation matchInformation = underTest.checkForMatches(toTest, set);
        assertEquals(1, matchInformation.numMatches);
    }
}
