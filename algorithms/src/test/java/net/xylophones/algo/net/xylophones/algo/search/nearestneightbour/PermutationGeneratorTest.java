package net.xylophones.algo.net.xylophones.algo.search.nearestneightbour;

import net.xylophones.algo.search.nearestneightbour.PermutationGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PermutationGeneratorTest {

    PermutationGenerator underTest;

    @Before
    public void setUp() {
        underTest = new PermutationGenerator();
    }

    @Test
    public void testOnePermutations() {
        List<int[]> permutations = underTest.generatePermutations(1);
        assertEquals(1, permutations.size());
    }

    @Test
    public void testTwoPermutations() {
        List<int[]> permutations = underTest.generatePermutations(2);
        assertEquals(2, permutations.size());
    }

    @Test
    public void testThreePermutations() {
        List<int[]> permutations = underTest.generatePermutations(3);
        assertEquals(6, permutations.size());
    }

}
