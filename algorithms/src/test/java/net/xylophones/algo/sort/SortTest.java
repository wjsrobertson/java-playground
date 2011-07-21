package net.xylophones.algo.sort;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import com.google.common.collect.Lists;

public class SortTest {
	
	BubbleSort bubbleSort = new BubbleSort();
	
	InsertionSort insertionSort = new InsertionSort();
	
	MergeSort mergeSort = new MergeSort();
	
	SelectionSort selectionSort = new SelectionSort();
	
	QuickSort quickSort = new QuickSort();

	@Test
	public void testBubbleSort() {
		List<Integer> sortMe = Lists.newArrayList(1, 3, 2, 5, 4, 6);
		List<Integer> expected = Lists.newArrayList(1, 2, 3, 4, 5, 6);
		
		bubbleSort.sort(sortMe);
		
		assertEquals(expected, sortMe);
	}
	
	@Test
	public void testInsertionSort() {
		List<Integer> sortMe = Lists.newArrayList(6, 1, 3, 2, 5, 4);
		List<Integer> expected = Lists.newArrayList(1, 2, 3, 4, 5, 6);
		
		insertionSort.sort(sortMe);
		
		assertEquals(expected, sortMe);
	}
	
	@Test
	public void testMergeSort() {
		List<Integer> sortMe = Lists.newArrayList(6, 1, 3, 2, 5, 4);
		List<Integer> expected = Lists.newArrayList(1, 2, 3, 4, 5, 6);
		
		mergeSort.sort(sortMe);
		
		assertEquals(expected, sortMe);
	}
	
	@Test
	public void testSelectionSort() {
		List<Integer> sortMe = Lists.newArrayList(6, 1, 3, 2, 5, 4);
		List<Integer> expected = Lists.newArrayList(1, 2, 3, 4, 5, 6);
		
		selectionSort.sort(sortMe);
		
		assertEquals(expected, sortMe);
	}
	
	@Test
	public void testQuickSort() {
		List<Integer> sortMe = Lists.newArrayList(6, 1, 3, 2, 5, 4);
		List<Integer> expected = Lists.newArrayList(1, 2, 3, 4, 5, 6);
		
		quickSort.sort(sortMe);
		
		assertEquals(expected, sortMe);
	}
	
	
}
