package net.xylophones.java8;

public class InterfacePlayground {

    public static void main(String[] args) {
        Shape rect = new Shape() {
            @Override
            public int width() {
                return 10;
            }

            @Override
            public int height() {
                return 20;
            }
        };

        System.out.println(String.format("area: %s", rect.area()));
    }

    public interface Shape {

        int width();

        int height();

        default int area() {
            return width() * height();
        }
    }

}
