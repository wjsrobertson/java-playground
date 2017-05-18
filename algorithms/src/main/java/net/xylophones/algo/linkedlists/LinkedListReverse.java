package net.xylophones.algo.linkedlists;

import java.util.LinkedList;
import java.util.ListIterator;

public class LinkedListReverse {

    public static void main(String[] args) {
        new LinkedListReverse().reverse();
    }

    public void reverse() {
        LinkedList<Integer> examplelist = createExamplelist();

        ListIterator<Integer> forward = examplelist.listIterator();
        int size = examplelist.size();
        int midPoint = size / 2;
        ListIterator<Integer> backward = examplelist.listIterator(size);

        for(int i=0 ; i<midPoint ; i++) {
            Integer f = forward.next();
            Integer b = backward.previous();

            forward.set(b);
            backward.set(f);
        }

        System.out.println(examplelist);
    }

    private LinkedList<Integer> createExamplelist() {
        LinkedList<Integer> forward = new LinkedList<>();
        forward.add(1);
        forward.add(2);
        forward.add(3);
        forward.add(4);
        forward.add(5);

        return forward;
    }

}
