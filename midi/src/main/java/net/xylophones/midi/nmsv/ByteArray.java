package net.xylophones.midi.nmsv;

import java.util.Arrays;

public class ByteArray {
    final byte[] bytes;

    ByteArray(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ByteArray)) {
            return false;
        }

        ByteArray byteArray = (ByteArray) obj;
        return Arrays.equals(byteArray.bytes, bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    @Override
    public String toString() {
        return Arrays.toString(bytes);
    }
}