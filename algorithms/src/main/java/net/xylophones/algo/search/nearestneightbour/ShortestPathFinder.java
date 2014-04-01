package net.xylophones.algo.search.nearestneightbour;

import java.util.List;

import static java.lang.Math.sqrt;

public class ShortestPathFinder {

    private PointPermutationGenerator permutationGenerator;

    public ShortestPathFinder(PointPermutationGenerator permutationCalculator) {
        this.permutationGenerator = permutationCalculator;
    }

    public Point[] sortShortestPath(Point[] points) {
        List<Point[]> permutations = permutationGenerator.generatePointPermutations(points);

        int minDistance = Integer.MAX_VALUE;
        Point[] permutationWithMinDistance = null;

        for (Point[] permutation: permutations) {
            int distance = calculateDistance(permutation);
            if (distance < minDistance) {
                permutationWithMinDistance = permutation;
            }
        }

        return permutationWithMinDistance;
    }

    private int calculateDistance(Point[] permutation) {
        return 0;  //To change body of created methods use File | Settings | File Templates.
    }


}
