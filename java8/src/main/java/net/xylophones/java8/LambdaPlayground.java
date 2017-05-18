package net.xylophones.java8;

import java.time.LocalDate;
import java.util.function.Supplier;

public class LambdaPlayground {

    public static void main(String[] args) {
        createSomeLambdas();
    }

    public static void createSomeLambdas() {
        Supplier<LocalDate> s1 = LocalDate::now;
        Supplier<LocalDate> s2 = () -> LocalDate.now();

        System.out.println(s1);
    }

}
