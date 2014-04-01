package net.xylophones.algo.search.citibank;

import java.util.Set;

public class MazeNode extends Node {
	
	public final boolean hasGold;
	
	public MazeNode(String id, Set<Node> children, boolean hasGold) {
		super(id, children);
		this.hasGold = hasGold;
	}
	
}
