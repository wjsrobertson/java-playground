package net.xylophones.algo.search.nearestneightbour;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermutationGenerator {

    public List<int[]> generatePermutations(int depth) {
        int[] currentPositions = new int[depth];

        int numPermutations = factorial(depth);
        List<int[]> permutations = new ArrayList<>(numPermutations);
        Integer currentDepth = 0;
        permute(new HashSet<Integer>(), currentDepth, currentPositions, permutations);

        return permutations;
    }

    private static void permute(Set<Integer> usedPositions, Integer currentDepth, int[] currentValues, List<int[]> results) {
        int maxDepth = currentValues.length;
        currentDepth++;
        if (currentDepth > maxDepth) {
            return;
        }

        for (int i=0 ; i<maxDepth ; i++) {
            if (! usedPositions.contains(i)) {
                usedPositions.add(i);
                currentValues[currentDepth-1] = i;

                if (currentDepth == maxDepth) {
                    results.add(currentValues);
                } else {
                    permute(usedPositions, currentDepth, currentValues, results);
                }
                usedPositions.remove(Integer.valueOf(i));
            }
        }
        currentDepth--;
    }

    private static int factorial(int number) {
        int total = 1;
        while (number > 1) {
            total *= number--;
        }
        return total;
    }

}
