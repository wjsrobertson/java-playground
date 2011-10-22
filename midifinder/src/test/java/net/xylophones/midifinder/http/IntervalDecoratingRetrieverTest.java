package net.xylophones.midifinder.http;

import static org.mockito.Mockito.*;

import java.io.IOException;

import junit.framework.TestCase;

public class IntervalDecoratingRetrieverTest extends TestCase {

	public void testWait() throws IOException {
		Retriever retrieverMock = mock(Retriever.class);
		IntervalDecoratingRetriever underTest = new IntervalDecoratingRetriever(retrieverMock);
		
		long before = System.currentTimeMillis();
		underTest.getBody("http://www.google.com");
		underTest.getBody("http://www.google.com");
		long after = System.currentTimeMillis();
		
		assertTrue( after - before > 1000 );
	}
	
}
