package net.xylophones.algo.cycledetection;

public class TortoiseAndHare2 {

    public static void main(String[] args) {

    }

    public static void x() {
        int x0 = 2;

        int tortoise = f(x0);
        int hare = f(f(x0));

        while(tortoise != hare) {
            tortoise = f(x0);
            hare = f(f(x0));
        }
    }

    private static int f(int x) {
        return 2;
    }





}
