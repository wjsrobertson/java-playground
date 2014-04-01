package net.xylophones.algo.search.nearestneightbour;

import java.util.ArrayList;
import java.util.List;

public class PointPermutationGenerator {

    private PermutationGenerator indexPermutationGenerator;

    public PointPermutationGenerator(PermutationGenerator permutationCalculator) {
        this.indexPermutationGenerator = permutationCalculator;
    }

    public List<Point[]> generatePointPermutations(Point[] points) {
        List<int[]> indexPermutations = indexPermutationGenerator.generatePermutations(points.length);
        List<Point[]> results = new ArrayList<>(points.length);

        for (int[] indexPermutation: indexPermutations) {
            Point result[] = new Point[points.length];

            for (int i=0 ; i<indexPermutation.length ; i++) {
                int position = indexPermutation[i];
                result[i] = points[position];
            }

            results.add(result);
        }

        return results;
    }

}
