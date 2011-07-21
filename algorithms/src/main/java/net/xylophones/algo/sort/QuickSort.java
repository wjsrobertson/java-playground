package net.xylophones.algo.sort;

import java.util.ArrayList;
import java.util.List;

public class QuickSort {

	public <T extends Comparable<? super T>> void sort(List<T> elements) {
		List<T> result = quickSort(elements);

		elements.clear();
		elements.addAll(result);
	}

	private <T extends Comparable<? super T>> List<T> quickSort(List<T> elements) {
		if (elements.size() <= 1) {
			return elements;
		}

		T pivot = elements.remove(elements.size() / 2);
		List<T> lower = new ArrayList<T>();
		List<T> higher = new ArrayList<T>();

		for (T current: elements) {
			if (current.compareTo(pivot) <= 0) {
				lower.add(current);
			} else {
				higher.add(current);
			}
		}

		return join(quickSort(lower), pivot, quickSort(higher));
	}

	private <T extends Comparable<? super T>> List<T> join(List<T> lower, T pivot, List<T> higher) {
		List<T> results = new ArrayList<T>(lower.size() + higher.size() + 1);
		results.addAll(lower);
		results.add(pivot);
		results.addAll(higher);

		return results;
	}	
}
