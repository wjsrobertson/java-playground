package net.xylophones.algo.cycledetection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TortoiseAndHare {

    private static Map<Integer,Integer> wikipediaMap = createWikipediaMap();

    public static void main(String[] args) {
        int x0 = 2;

        int slow = f(x0);
        int fast = f(f(x0));
        while (slow != fast) {
            slow = f(slow);
            fast = f(f(fast));
        }

        int mu = 0;
        slow = x0;
        while(slow != fast) {
            slow = f(slow);
            fast = f(fast);
            mu += 1;
        }

        System.out.println(fast + ", " + mu);
    }

    private static Integer f(int x0) {
        return wikipediaMap.get(x0);
    }

    private static Integer getNumberAt(int index) {
        int value = 2;
        for (int i=0 ; i<index ; i++) {
            value = f(value);
        }

        return value;
    }

    private static Map<Integer,Integer> createWikipediaMap() {
        Map<Integer,Integer> map = new HashMap<>();
        map.put(0, 6);
        map.put(1, 6);
        map.put(2, 0);
        map.put(3, 1);
        map.put(4, 4);
        map.put(5, 3);
        map.put(6, 3);
        map.put(7, 4);
        map.put(8, 0);

        return map;
    }


}
