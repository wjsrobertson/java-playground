package net.xylophones.java5.generics;

import java.util.ArrayList;
import java.util.List;

public class AddWrongTypeToGenericClass {

    public static void main(String[] args) {
        AddWrongTypeToGenericClass app = new AddWrongTypeToGenericClass();
        app.insertWrongType();
    }

    private void insertWrongType() {
        List<String> strings = new ArrayList<String>();
        addElement(strings);

        String string = strings.get(0); // throws ClassCastException
        System.out.println("String is: " + string);
    }

    private void addElement(List strings) {
        strings.add(new Integer(100));
    }

}
