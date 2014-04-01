package net.xylophones.midi.nmsv;

import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.List;

public class ByteArrayFrequencyCounter {

    public MatchInformation checkForMatches(ByteArray sequence, byte[] fileContents) {
        int sequenceLength = sequence.bytes.length;

        List<Integer> matchOffsets = new ArrayList<>();
        for (int i=0 ; i<=fileContents.length-sequenceLength ; i++) {
            boolean matches = true;

            for (int j=0 ; j<sequenceLength ; j++) {
                if (sequence.bytes[j] != fileContents[i+j]) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                matchOffsets.add(i);
            }
        }

        if (matchOffsets.isEmpty()) {
            return MatchInformation.NO_MATCH;
        } else {
            return new MatchInformation(matchOffsets.size(), Ints.toArray(matchOffsets), sequence);
        }
    }

}
