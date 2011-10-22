package net.xylophones.java7;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ForkJoinMergeSort extends RecursiveTask<List<Integer>> {

    private enum SortMode {
        FORK_JOIN, SERIAL
    }

    private static final int MIN_PARALLEL_PROCESS_SIZE = 5;

    private List<Integer> elements;

    public ForkJoinMergeSort(List<Integer> elements) {
        this.elements = elements;
    }

    @Override
    protected List<Integer> compute() {
        if (elements.size() < MIN_PARALLEL_PROCESS_SIZE) {
            return sort(elements, SortMode.SERIAL);
        }

        return sort(elements, SortMode.FORK_JOIN);
    }

    private List<Integer> sort(List<Integer> elements, SortMode sortMode) {
        if (elements.size() <= 1) {
			return elements;
		}
        
        int middleIndex = elements.size() / 2;
        List<Integer> left = new ArrayList<>( elements.subList(0, middleIndex) );
		List<Integer> right = new ArrayList<>( elements.subList(middleIndex, elements.size()) );

        if (sortMode == SortMode.FORK_JOIN) {
            left = new ForkJoinMergeSort(left).compute();
            right = new ForkJoinMergeSort(right).compute();
        } else {
            left = sort(left, SortMode.SERIAL);
            right = sort(right, SortMode.SERIAL);
        }

        return merge(left, right);
	}

	private List<Integer> merge(List<Integer> left, List<Integer> right) {
		List<Integer> result = new ArrayList<>( left.size() + right.size() );
		
		while (left.size() > 0 || right.size() > 0) {
			if (left.size() > 0 && right.size() > 0) {
				Integer firstLeft = left.get(0);
				Integer firstRight = right.get(0);
				if (firstLeft.compareTo(firstRight) <= 0) {
					Integer append = left.remove(0);
					result.add(append);
				} else {
					Integer append = right.remove(0);
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

