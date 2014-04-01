package net.xylophones.algo.search.citibank;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class Searcher<T> {

	public T traverse(Node root, NodeProcessor<T> processor) {
		if (! root.hasChildren()) {
			return null;
		}
		
		Set<String> processedNodeIds = new HashSet<String>();
		Deque<Node> pendingNodes = new ArrayDeque<Node>();
		pendingNodes.add(root);
		
		while (! pendingNodes.isEmpty()) {
			Node current = pendingNodes.removeFirst();
			
			if (current.hasChildren()) {
				for (Node child: current.neighbours) {
					if (! processedNodeIds.contains(child.id)) {
						pendingNodes.addLast(child);												
					}
				}
			}
			
			T result = processor.process(current);
			processedNodeIds.add(current.id);
			if (result != null) {
				return result;
			}
		}
		
		return null;
	}
}
