package net.xylophones.algo.sort;

import java.util.List;

public class InsertionSort {

	public <T extends Comparable<? super T>> void sort(List<T> elements) {
		for (int i=1 ; i<elements.size() ; i++) {
			T value = elements.get(i);
			int j = i-1;
			boolean done = false;
			while (! done) {
				T current = elements.get(j);
				if (current.compareTo((T)value) > 0) {
					elements.set(j+1, current);
					if (--j < 0) {
						done = true;
					}
				} else {
					done = true;
				}
			}
			elements.set(j+1, value);
		}
	}
	
}

