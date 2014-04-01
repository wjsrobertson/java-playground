package net.xylophones.algo.patterns.visitor.bookexample;

public interface Visitor {

    void visit(Book book);

    void visit(Video cd);

}
