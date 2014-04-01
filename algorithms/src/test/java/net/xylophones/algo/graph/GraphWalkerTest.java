package net.xylophones.algo.graph;

import org.junit.Test;

import java.util.List;

import static net.xylophones.algo.graph.GraphMother.createTree;
import static org.apache.commons.lang3.StringUtils.join;
import static org.junit.Assert.assertEquals;

public class GraphWalkerTest {

    private GraphWalker underTest = new GraphWalker();

    @Test
    public void traversePreOrder() {
        List<String> values = underTest.walkDepthFirstPreOrder(createTree());
        String actual = join(values, ", ");

        String expected = "F, B, A, D, C, E, G, I, H"; // depth first pre-order
        assertEquals(expected, actual);
    }

    @Test
    public void traverseInOrder() {
        List<String> values = underTest.walkDepthFirstInOrder(createTree());
        String actual = join(values, ", ");

        String expected = "A, B, C, D, E, F, G, H, I"; // in-order
        assertEquals(expected, actual);
    }





            //String expected = "F, B, G, A, D, I, C, E, H"; // breadth first


}
