package net.xylophones.algo.sort;

import java.util.ArrayList;
import java.util.List;

public class MergeSort {

	public <T extends Comparable<? super T>> void sort(List<T> elements) {
		List<T> result = mergeSort(elements);
		
		elements.clear();
		elements.addAll(result);
	}

	private <T extends Comparable<? super T>> List<T> mergeSort(List<T> elements) {
		if (elements.size() <= 1) {
			return elements;
		}
		
		int middleIndex = elements.size() / 2;
		
		List<T> left = new ArrayList<T>( elements.subList(0, middleIndex) );
		List<T> right = new ArrayList<T>( elements.subList(middleIndex, elements.size()) );
		
		left = mergeSort(left);
		right = mergeSort(right);
		
		return merge(left, right);
	}

	private <T extends Comparable<? super T>> List<T> merge(List<T> left, List<T> right) {
		List<T> result = new ArrayList<T>( left.size() + right.size() );
		
		while (left.size() > 0 || right.size() > 0) {
			if (left.size() > 0 && right.size() > 0) {
				T firstLeft = left.get(0);
				T firstRight = right.get(0);
				if (firstLeft.compareTo(firstRight) <= 0) {
					T append = left.remove(0);
					result.add(append);
				} else {
					T append = right.remove(0);
					result.add(append);
				}
			} else if (left.size() > 0) {
				result.addAll(left);
				left.clear();
			} else if (right.size() > 0) {
				result.addAll(right);
				right.clear();
			}
		}

		return result;
	}

}

