package net.xylophones.midi.nmsv;

import org.apache.commons.collections.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.apache.commons.io.FileUtils.readFileToByteArray;

public class Application {

    private SequencePatternChecker sequencePatternChecker;

    public Application() {
        sequencePatternChecker = new SequencePatternChecker(
            new SequenceSetGenerator(), new ByteArrayFrequencyCounter()
        );
    }

    public void process() throws IOException {
        //String path = "D:\\Users\\Will\\Projects\\Music\\Patch Libraries\\Rankin Audio Massive Preset Freebies\\Chord Lead - rankinaudio.nmsv";
        String path="D:\\temp\\Rankin Audio Massive Preset Freebies\\Sharp Lead - rankinaudio.com.nmsv";
        byte[] data = readFileToByteArray(new File(path));

        int numMatches = -1;
        int sequenceLength = 1;
        while (numMatches != 0) {
            Map<ByteArray,MatchInformation> byteArrayMatchInformationMap
                    = filterToMatchesOnly( sequencePatternChecker.determineSequenceFrequency(data, sequenceLength) );

            numMatches = byteArrayMatchInformationMap.size();
            List<MatchInformation> values = sortMatches(byteArrayMatchInformationMap.values());

            System.out.println(sequenceLength + "," + numMatches + "," + Arrays.toString(values.toArray()));
            sequenceLength++;
        }

        System.out.println("file is " + data.length + " bytes");
    }

    private List<MatchInformation> sortMatches(Collection<MatchInformation> values) {
        List<MatchInformation> toSort = new ArrayList<>(values);
        Collections.sort( toSort, new Comparator<MatchInformation>() {
            @Override
            public int compare(MatchInformation o1, MatchInformation o2) {
                return o2.numMatches-o1.numMatches;
            }
        } );
        return toSort;
    }

    private Map<ByteArray,MatchInformation> filterToMatchesOnly(Map<ByteArray,MatchInformation>matchInformation) {
        Map<ByteArray,MatchInformation> filtered = new HashMap<>();

        for (Map.Entry<ByteArray, MatchInformation> entry : matchInformation.entrySet()) {
            if (entry.getValue().numMatches > 1) {
                filtered.put(entry.getKey(), entry.getValue());
            }
        }

        return filtered;
    }

}
