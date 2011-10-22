package net.xylophones.algo.shuffle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShuffleMoveLess {

	public <T extends Object> void shuffle(List<T> list) {
		Set<Integer> moved = new HashSet<Integer>();
		
		for (int i=0 ; i<list.size() ; i++) {
			if (! moved.contains(i)) {
				int random = (int) ((Math.random() * list.size()) - 1);

				T move = list.get(i);
				list.set(i, list.get(random));
				list.set(random, move);
				
				moved.add(i);
				moved.add(random);
			}
		}
	}
}
