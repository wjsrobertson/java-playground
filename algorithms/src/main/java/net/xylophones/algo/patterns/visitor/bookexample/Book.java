package net.xylophones.algo.patterns.visitor.bookexample;


public class Book implements Visitable {

    private String title;

    private int value;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
