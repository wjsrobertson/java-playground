package net.xylophones.algo.net.xylophones.algo.search.nearestneightbour;

import net.xylophones.algo.search.nearestneightbour.PermutationGenerator;
import net.xylophones.algo.search.nearestneightbour.Point;
import net.xylophones.algo.search.nearestneightbour.PointPermutationGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PointPermutationGeneratorTest {

    @Mock PermutationGenerator permutationCalculator;
    List<int[]> integerPermutations;
    Point p1 = new Point(1,2);
    Point p2 = new Point(3,4);
    Point[] points;

    PointPermutationGenerator underTest;

    @Before
    public void setUp() {
        integerPermutations = new ArrayList<int[]>();
        integerPermutations.add(new int[]{0, 1});
        integerPermutations.add(new int[]{1, 0});
        points = new Point[]{p1, p2};
        underTest = new PointPermutationGenerator(permutationCalculator);
    }

    @Test
    public void testPointsArePermutedAccordingToIntegerPermutations() {
        when(permutationCalculator.generatePermutations(2)).thenReturn(integerPermutations);

        List<Point[]> permutedPoints = underTest.generatePointPermutations(points);
        assertEquals(2, permutedPoints.size());

        assertEquals( p1, permutedPoints.get(0)[0] );
        assertEquals( p2, permutedPoints.get(0)[1] );

        assertEquals( p2, permutedPoints.get(1)[0] );
        assertEquals( p1, permutedPoints.get(1)[1] );
    }

}
