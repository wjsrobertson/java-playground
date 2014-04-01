package net.xylophones.algo;

import static net.xylophones.algo.search.citibank.CellContents.*;

import net.xylophones.algo.search.citibank.CellContents;
import net.xylophones.algo.search.citibank.MazeGenerator;
import net.xylophones.algo.search.citibank.MazeNode;
import net.xylophones.algo.search.citibank.Node;
import net.xylophones.algo.search.citibank.NodeProcessor;
import net.xylophones.algo.search.citibank.Searcher;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchTest {
	
	private static final Logger log = LoggerFactory.getLogger(SearchTest.class);

	private MazeGenerator mazeGenerator = new MazeGenerator();
	
	@Test
	public void testX() {
		CellContents[][] contents = {
			{WALL, WALL, WALL, WALL, PATH, PATH},
			{PATH, WALL, PATH, WALL, WALL, PATH},
			{PATH, WALL, PATH, WALL, PATH, PATH},
			{PATH, PATH, PATH, PATH, PATH, PATH},
			{WALL, WALL, WALL, WALL, PATH, PATH},
			{PATH, PATH, PATH, GOLD, PATH, PATH},
			{PATH, WALL, PATH, PATH, PATH, PATH}
		};
		
		Node firstMazeNode = mazeGenerator.generateMaze(contents); 
		
		NodeProcessor<String> processor = new NodeProcessor<String>() {
			public String process(Node node) {
				log.info("Testing for gold at {}", node);
				if (((MazeNode)node).hasGold) {
					return node.id;
				}
				return null;
			}
		};
		
		Searcher<String> traverser = new Searcher<String>();
		String result = traverser.traverse(firstMazeNode, processor);
		
		if (result != null) {
			log.info("Found the gold at {}", result);			
		} else {
			log.info("Couldn't find any gold");		
		}
	}
}
