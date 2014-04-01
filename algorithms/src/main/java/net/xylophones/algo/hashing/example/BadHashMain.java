package net.xylophones.algo.hashing.example;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class BadHashMain {

    public static void main(String[] args) {
        MyKey k1 = new MyKey(1);
        MyKey k2 = new MyKey(2);

        Map<MyKey,String> map = new HashMap<>();
        map.put(k1,"k1");
        map.put(k2,"k2");

       System.out.println(map.get(k1));
    }

    static class MyKey {

        int num;

        MyKey(int num) {
            this.num = num;
        }

        @Override
        public boolean equals(Object obj) {
            return ((MyKey) obj).num == num;
        }

        @Override
        public int hashCode() {
            return num++;
        }
    }

}
