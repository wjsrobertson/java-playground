package net.xylophones.algo.sort;

import java.util.List;

public class BubbleSort {

	public <T extends Comparable<? super T>> void sort(List<T> elements) {
		boolean checkListAgain = true;
		while(checkListAgain) {
			boolean swapMade = false;
			
			for (int i=1 ; i<elements.size() ; i++) {
				T left = elements.get(i-1);
				T right = elements.get(i);
				
				if (left.compareTo( right) > 0) {
					elements.set(i-1, right);
					elements.set(i, left);			
					swapMade = true;
				}
			}
			
			checkListAgain = swapMade;
		}
	}
}
