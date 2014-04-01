package net.xylophones.midi.nmsv;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.google.inject.internal.Sets.newHashSet;

public class SequenceSetGenerator {

    public Set<ByteArray> createSequenceSets(byte[] data, int sequenceLength) {
        Set<ByteArray> sequences = new LinkedHashSet<>();

        int numSequences = data.length - sequenceLength + 1;
        for (int i=0 ; i<numSequences; i++ ) {
            byte[] nextResult = new byte[sequenceLength];

            for (int j=0 ; j<sequenceLength ; j++) {
                nextResult[j] = (byte) (data[i+j] & 0xFF);
            }

            sequences.add(new ByteArray(nextResult));
        }

        return sequences;
    }


}
