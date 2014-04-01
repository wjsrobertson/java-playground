package net.xylophones.algo.search.nomura;

import java.util.Deque;
import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkNotNull;

public class SearchRunner {

    /*
    a
   b c
  d   f
 g   j k
     */

    static class Node {
        Node(String element, Node left, Node right) {
            this.left = left;
            this.right = right;
            this.element = element;
        }
        final Node left;
        final Node right;
        final String element;
    }

    public static void main(String[] args) {
        SearchRunner searchRunner = new SearchRunner();
        Node graph = searchRunner.createWikipediaGraph();
        searchRunner.breadthFirst(graph);
    }

    private Node createGraph() {
        Node g = new Node("g", null, null);
        Node d = new Node("d", g, null);
        Node b = new Node("b", d, null);

        Node j = new Node("j", null, null);
        Node k = new Node("k", null, null);

        Node f = new Node("f", j, k);
        Node c = new Node("c", null, f);

        Node a = new Node("a", b, c);

        return a;
    }

    private Node createWikipediaGraph() {
        Node c = new Node("c", null, null);
        Node e = new Node("e", null, null);
        Node h = new Node("h", null, null);

        Node a = new Node("a", null, null);
        Node d = new Node("d", c, e);
        Node i = new Node("i", h, null);

        Node b = new Node("b", a, d);
        Node g = new Node("g", null, i);

        Node f = new Node("f", b, g);

        return f;
    }

    public void breadthFirst(Node node) {
        checkNotNull(node);

        Deque<Node> nodes = new LinkedList<>();
        nodes.add(node);

        while(nodes.size() > 0) {
            Node currentNode = nodes.pop();
            System.out.println(currentNode.element);

            if (currentNode.left != null) {
                nodes.push(currentNode.left);
            }
            if (currentNode.right != null) {
                nodes.push(currentNode.right);
            }
        }

        /*
        breadthFirst(node.left);
        breadthFirst(node.right);
        System.out.print(node.element);
        */

        //String expected = "abcdfgjk";
    }

    public void depthFirst(Node node) {
        String expected = "gdbajfck";
    }


}
