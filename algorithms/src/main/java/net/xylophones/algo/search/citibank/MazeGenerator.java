package net.xylophones.algo.search.citibank;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MazeGenerator {
	
	private static final Logger log = LoggerFactory.getLogger(MazeGenerator.class);
		
	public Node generateMaze(CellContents[][] contents) {
		
		Map<String,MazeNode> nodes = new HashMap<String,MazeNode>();
		
		int height = contents[0].length;
		int width = contents.length;
		Node first = null;
		
		log.info("Generating maze - dimensions: {} by {}", width, height);

		for (int x=0 ; x<width ; x++) {
			for (int y=0; y<height ; y++) {
				if (contents[x][y] != CellContents.WALL) {
					MazeNode current = getOrCreateNode(nodes, x, y, contents[x][y]);
					
					if (y > 0 && contents[x][y-1] != CellContents.WALL) {
						MazeNode down = getOrCreateNode(nodes, x, y-1, contents[x][y-1]);
						current.neighbours.add(down);
					}
					if (y < height-1 && contents[x][y+1] != CellContents.WALL) {
						MazeNode up = getOrCreateNode(nodes, x, y+1, contents[x][y+1]);
						current.neighbours.add(up);
					}
					if (x > 0 && contents[x-1][y] != CellContents.WALL) {
						MazeNode left = getOrCreateNode(nodes, x-1, y, contents[x-1][y]);
						current.neighbours.add(left);
					}
					if (x < width-1 && contents[x+1][y] != CellContents.WALL) {
						MazeNode right = getOrCreateNode(nodes, x+1, y, contents[x+1][y]);
						current.neighbours.add(right);
					}
					
					if (first == null) {
						first = current;
					}
				}
			}
		}
		
		return first;
	}

	MazeNode getOrCreateNode(Map<String, MazeNode> nodes, int i, int j, CellContents contents) {
		String name = i + "x" + j;
		MazeNode node = nodes.get(name);
		if (node == null) {
			node = new MazeNode(name, new HashSet<Node>(), contents == CellContents.GOLD);		
			nodes.put(name, node);
		}
		
		return node;
	}
}
