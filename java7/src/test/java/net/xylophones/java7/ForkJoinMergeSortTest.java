package net.xylophones.java7;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import javax.xml.ws.ServiceMode;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.assertEquals;

public class ForkJoinMergeSortTest {

    List<Integer> sortMe = Lists.newArrayList(6, 1, 3, 2, 5, 4, 8, 7, 9, 11, 10, 14, 13, 12, 19, 18, 17, 16, 15);

    List<Integer> expected = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19);

	ForkJoinMergeSort mergeSort;
    
    @Before
    public void setUp() {
         mergeSort = new ForkJoinMergeSort(sortMe);
    }

	@Test
	public void testMergeSort() {
        ForkJoinPool pool = new ForkJoinPool();
        List<Integer> sorted = pool.invoke(mergeSort);

		assertEquals(expected, sorted);
	}

}
