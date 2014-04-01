package net.xylophones.algo.graph;

import com.google.common.collect.Lists;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;

public class GraphWalker {

    public List<String> walkDepthFirstPreOrder(BinaryNode<String> root) {
        List<String> values = Lists.newArrayList();

        LinkedList<BinaryNode> nodes = newLinkedList();
        nodes.push(root);

        while (! nodes.isEmpty()) {
            BinaryNode<String> current = nodes.pop();
            values.add(current.value);

            if (current.right != null) {
               nodes.push(current.right);
            }
            if (current.left != null) {
               nodes.push(current.left);
            }
        }

        return values;
    }

    public List<String> walkDepthFirstInOrder(BinaryNode<String> root) {
        List<String> values = Lists.newArrayList();
        LinkedList<BinaryNode> nodes = newLinkedList();
        BinaryNode currentNode = root;

        while (! nodes.isEmpty() || currentNode != null) {
            if (currentNode != null) {
                nodes.push(currentNode);
                currentNode = currentNode.left;
            } else {
                BinaryNode<String> current = nodes.pop();
                values.add(current.value);
                currentNode = current.right;
            }
        }

        return values;
    }

    public List<String> walkDepthFirstPostOrder(BinaryNode<String> root) {
        /*
        List<String> values = Lists.newArrayList();
        LinkedList<BinaryNode> nodes = newLinkedList();
        BinaryNode currentNode = root;

        while (! nodes.isEmpty() || currentNode != null) {
            if (currentNode != null) {
                nodes.push(currentNode);
                currentNode = currentNode.left;
            } else {
                BinaryNode<String> current = nodes.pop();
                values.add(current.value);
                currentNode = current.right;
            }
        }
        */

        return null;
    }

}
