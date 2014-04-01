package net.xylophones.algo.patterns.visitor.bookexample;

public class Video implements Visitable {

    private String title;

    private int price;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
