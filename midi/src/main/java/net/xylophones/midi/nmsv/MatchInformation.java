package net.xylophones.midi.nmsv;

public class MatchInformation {

    public final int numMatches;

    public final int[] matchStartIndexes;

    public final ByteArray matchingByteArray;

    public static final MatchInformation NO_MATCH = new MatchInformation();

    private static final int[] EMPTY_INT_ARRAY = new int[]{};

    private MatchInformation() {
        this.numMatches = 0;
        this.matchStartIndexes = EMPTY_INT_ARRAY;
        this.matchingByteArray = null;
    }

    public MatchInformation(int numMatches, int[] matchStartIndexes, ByteArray matchingByteArray) {
        this.numMatches = numMatches;
        this.matchStartIndexes = matchStartIndexes;
        this.matchingByteArray = matchingByteArray;
    }

    @Override
    public String toString() {
        return numMatches + ":" + matchingByteArray;
    }
}
