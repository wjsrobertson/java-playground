package net.xylophones.algo.linkedlists;

public class SingleLinkedListReverse {

    static class Node {
        int value;
        Node next;

        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    public static void main(String[] args) {
        Node list = createList();
        printList(list);
        Node reverse = reverse(list);
        printList(reverse);
    }

    private static Node reverse(Node current) {
        Node next = null;
        Node prev = null;

        while (current != null) {
            next = current.next;
            current.next = prev;

            prev = current;
            current = next;
        }

        return prev;
    }

    private static Node createList() {
        Node _5 = new Node(5, null);
        Node _4 = new Node(4, _5);
        Node _3 = new Node(3, _4);
        Node _2 = new Node(2, _3);
        Node _1 = new Node(1, _2);

        return _1;
    }

    private static String nodeDesc(Node node) {
        if (node == null) {
            return "| | ";
        } else if (node.next == null) {
            return "|" + node.value + "| ";
        } else {
            return "|" + node.value + "|" + node.next.value;
        }
    }

    private static void printList(Node node) {
        StringBuilder s = new StringBuilder();
        s.append(String.valueOf(node.value) + " ");

        while (node.next != null) {
            node = node.next;
            s.append(String.valueOf(node.value) + " ");
        }

        System.out.println(s);
    }
}
