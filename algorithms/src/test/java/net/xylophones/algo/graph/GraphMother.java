package net.xylophones.algo.graph;

public class GraphMother {

    public static BinaryNode<String> createTree() {
        BinaryNode<String> a = new BinaryNode("A");
        BinaryNode<String> b = new BinaryNode("B");
        BinaryNode<String> c = new BinaryNode("C");
        BinaryNode<String> d = new BinaryNode("D");
        BinaryNode<String> e = new BinaryNode("E");
        BinaryNode<String> f = new BinaryNode("F");
        BinaryNode<String> g = new BinaryNode("G");
        BinaryNode<String> h = new BinaryNode("H");
        BinaryNode<String> i = new BinaryNode("I");

        f.left = b;
        f.right = g;
        b.left = a;
        b.right = d;
        d.left = c;
        d.right = e;
        g.right = i;
        i.left = h;

        return f;
    }

}
