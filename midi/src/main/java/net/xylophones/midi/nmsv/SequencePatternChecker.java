package net.xylophones.midi.nmsv;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SequencePatternChecker {

    private final SequenceSetGenerator sequenceSetGenerator;

    private final ByteArrayFrequencyCounter byteArrayFrequencyCounter;

    public SequencePatternChecker(SequenceSetGenerator sequenceSetGenerator, ByteArrayFrequencyCounter byteArrayFrequencyCounter) {
        this.sequenceSetGenerator = sequenceSetGenerator;
        this.byteArrayFrequencyCounter = byteArrayFrequencyCounter;
    }

    public Map<ByteArray,MatchInformation> determineSequenceFrequency(byte[] contents, int sequenceLength) throws IOException {
        Map<ByteArray,MatchInformation> results = new HashMap<>();

        Set<ByteArray> sequences = sequenceSetGenerator.createSequenceSets(contents, sequenceLength);
        for (ByteArray sequence: sequences) {
            MatchInformation matchInformation = byteArrayFrequencyCounter.checkForMatches(sequence, contents);
            results.put(sequence, matchInformation);
        }

        return results;
    }

}
