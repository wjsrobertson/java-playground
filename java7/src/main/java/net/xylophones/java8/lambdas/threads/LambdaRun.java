package net.xylophones.java8.lambdas.threads;

import java.util.Comparator;

public class LambdaRun {

    public static void main(String[] args) {
        Runnable r = () -> System.out.println("hi");
        Comparator<Integer> comp1 = (x, y) -> (x < y) ? -1 : 0;

        Comparator<Integer> comp = (x, y) -> {
            return (x < y) ? -1 : 0;
        };

    }

}
