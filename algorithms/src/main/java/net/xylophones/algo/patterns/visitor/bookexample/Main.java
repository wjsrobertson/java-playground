package net.xylophones.algo.patterns.visitor.bookexample;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        TotalVisitor visitor = new TotalVisitor();

        Book book1 = new Book();
        book1.setValue(200);
        book1.setTitle("Hello");

        Book book2 = new Book();
        book2.setValue(500);
        book2.setTitle("Hello The Revenge");

        Video video = new Video();
        video.setPrice(500);
        video.setTitle("Banana");

        List<Visitable> buying = new ArrayList<>();
        buying.add(book1);
        buying.add(book2);
        buying.add(video);

        for (Visitable visitable: buying) {
            visitable.accept(visitor);
        }

        System.out.println( "Total is: " + visitor.getTotal() );
    }

}
