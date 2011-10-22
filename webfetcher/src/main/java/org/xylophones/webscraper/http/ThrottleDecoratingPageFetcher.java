package org.xylophones.webscraper.http;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple class using decorator pattern to throttle the number of
 * concurrent web requests performed by the wrapped {@code PageFetcher}. 
 */
public class ThrottleDecoratingPageFetcher implements PageFetcher {

	/**
	 * Logging instance
	 */
	private static final Log log = LogFactory.getLog(TidyDecoratingPageFetcher.class);
	
	private final PageFetcher fetcher;
	
	private final Semaphore semaphore;
	
	public ThrottleDecoratingPageFetcher(PageFetcher fetcher, int numConcurrentRequests) {
		this.fetcher   = fetcher;
		this.semaphore = new Semaphore(numConcurrentRequests);
	}
	
	@Override
	public HttpResponse fetchPage(String url) throws IOException {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			// interupted, just allow execution
		}
		
		try {
			return fetcher.fetchPage(url);
		} finally {
			semaphore.release();
		}
	}
	
}
