package net.xylophones.algo.shuffle;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.internal.Lists;

public class ShuffleTest {
	
	private static final Logger log = LoggerFactory.getLogger(ShuffleTest.class);
	
	SimpleShuffle simpleShuffle = new SimpleShuffle();
	
	ShuffleMoveLess shuffleMoveLess = new ShuffleMoveLess();
	
	@Test
	public void testSimpleShuffle() {
		List<Integer> sorted = Lists.newArrayList(1, 2, 3, 4, 5, 6);
		simpleShuffle.shuffle(sorted);
		log.info(sorted.toString());
	}
	
	@Test
	public void testShuffleMoveLess() {
		List<Integer> sorted = Lists.newArrayList(1, 2, 3, 4, 5, 6);
		shuffleMoveLess.shuffle(sorted);
		log.info(sorted.toString());
	}
	
	
	
}
