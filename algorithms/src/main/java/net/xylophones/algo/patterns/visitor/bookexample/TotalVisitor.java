package net.xylophones.algo.patterns.visitor.bookexample;

public class TotalVisitor implements Visitor {

    private double total;

    @Override
    public void visit(Book book) {
        total += book.getValue();
    }

    @Override
    public void visit(Video cd) {
        total += cd.getPrice();
    }

    public double getTotal() {
        return total;
    }
}
