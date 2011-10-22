package net.xylophones.algo.shuffle;

import java.util.List;

public class SimpleShuffle {

	public <T extends Object> void shuffle(List<T> list) {
		for (int i=0 ; i<list.size() ; i++) {
			int random = (int) ((Math.random() * list.size()) - 1);
			T move = list.get(i);
			list.set(i, list.get(random));
			list.set(random, move);
		}
	}

}
