package net.xylophones.java7;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Language {

    public void testX() {
        int oneThousand = 1_000;

        Map<String,String> myMap = new HashMap<>();

        try {
            System.out.println("whee");
        } catch (NullPointerException | NumberFormatException e) {
            System.out.println("Whoohoo");
        }

        String myString = "1";

        switch (myString) {
            case "1":
                break;
            case "2":
                break;
        }

        try (
            InputStream is = new FileInputStream(new File(""));
            OutputStream os = new FileOutputStream(new File("out"));
        ) {
            is.available();
            os.hashCode();
        } catch (Exception e) {
            System.out.println("");
        }

        
    }

}
