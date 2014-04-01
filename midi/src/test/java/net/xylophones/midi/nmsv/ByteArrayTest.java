package net.xylophones.midi.nmsv;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ByteArrayTest {

    @Test
    public void testEquality() {
        ByteArray array1 = new ByteArray(new byte[]{0});
        ByteArray array2 = new ByteArray(new byte[]{0});

        assertEquals(array1, array2);
    }

}
