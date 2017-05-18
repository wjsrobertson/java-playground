package net.xylophones.java8;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.stream.Collector;

public class SupplierSandpit {

    public void x() {
        ArrayList<String> strings = new ArrayList<>();

        Collector<String, ?, TreeSet<String>> intoSet =
                Collector.of(TreeSet::new, TreeSet::add,
                        (left, right) -> { left.addAll(right); return left; });

        strings.stream().collect(intoSet);


    }

}
