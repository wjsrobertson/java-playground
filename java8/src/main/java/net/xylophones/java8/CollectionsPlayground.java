package net.xylocasephones.java8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CollectionsPlayground {

    public static void main(String[] args) {

    }

    private static void tryFlatMap() {
        List<String> words = new ArrayList<>();
        words.add("banana");
        words.add("apple");
        words.add("peach");

        words.stream()
            .flatMap(word -> Stream.of(word.split("")))
            .forEach(letter -> System.out.println(letter));
    }

    private static void maps() {
        Map<String,Integer> myMap = new HashMap<>();
        myMap.put("apple", 10);
        myMap.put("banana", 20);
        myMap.put("peach", 69);
    }

}
