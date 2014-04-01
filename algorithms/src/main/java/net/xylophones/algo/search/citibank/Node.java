package net.xylophones.algo.search.citibank;

import java.util.Set;

public class Node {

	public final String id;
	
	public final Set<Node> neighbours;
	
	public Node(String id, Set<Node> children) {
		this.id = id;
		this.neighbours = children;
	}

	public boolean hasChildren() {
		return neighbours != null && neighbours.size() > 0;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Node) {
			return ((Node) other).id == id;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return id;
	}
}
