package net.xylophones.algo.search.nearestneightbour;

import java.util.List;

import static java.lang.Math.sqrt;

/**
 * Class to determine the total length between a series of Point objects
 */
public class ChainLengthCalculator {

    public double calculateTotalLength(Point[] points) {
        double distance = 0;
        for (int i=0 ; i<points.length-1 ; i++) {
            int j = i+1;
            distance += calculateDistance(points[i], points[j]);
        }

        return distance;
    }

    private double calculateDistance(Point p1, Point p2) {
        int a = (p2.x - p1.x);
        int b = (p2.y - p1.y);
        return sqrt(a*a + b*b);
    }

}
