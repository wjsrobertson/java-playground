package net.xylophones.algo.sort;

import java.util.List;

public class SelectionSort {

	public <T extends Comparable<? super T>> void sort(List<T> elements) {
		for (int i=0 ; i<elements.size() ; i++) {
			int minIndex = -1;
			
			for (int j=i ; j<elements.size() ; j++) {
				if (minIndex == -1 || elements.get(j).compareTo(elements.get(minIndex)) < 0) {
					minIndex = j;
				}
			}
			
			if (minIndex != -1) {
				T tmp = elements.get(i);
				elements.set(i, elements.get(minIndex));
				elements.set(minIndex, tmp);
			}
		}
	}
}
