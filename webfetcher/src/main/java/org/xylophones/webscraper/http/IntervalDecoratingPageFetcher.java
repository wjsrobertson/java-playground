package org.xylophones.webscraper.http;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO - check thread to clear out map
 * 
 * Class to block requests and wait for a specific interval between requests
 * to the same host
 */
public class IntervalDecoratingPageFetcher implements PageFetcher {
	
	/**
	 * host -> request time mapping 
	 */
	private final ConcurrentHashMap<String,Long> map = new ConcurrentHashMap<String,Long>();
	
	private final long minRequestPeriod; 
	
	private final PageFetcher fetcher;
	
	private volatile boolean running = false;
	
	private volatile long cleanupThreadSleep = 30000;	// every half minute 
	
	private final StaleEntryDeleter staleEntryDeleter;
	
	public IntervalDecoratingPageFetcher(PageFetcher fetcher, int minRequestPeriod) {
		this.fetcher = fetcher;
		this.minRequestPeriod = minRequestPeriod;
		
		staleEntryDeleter = new StaleEntryDeleter();
		Thread thread = new Thread(staleEntryDeleter);
		thread.start();
	}
	
	@Override
	public HttpResponse fetchPage(String urlString) throws IOException {
		URL url = new URL(urlString);
		String host = url.getHost();
		
		// calculate wait time (if any)
		long requiredWait = -1;
		Long previousRequestTime = map.get(host);
		if (previousRequestTime != null) {
			requiredWait = System.currentTimeMillis() - previousRequestTime + minRequestPeriod;
		}
		
		// block & wait if required
		if ( requiredWait > 0 ) {
			try {
				Thread.sleep(requiredWait);
			} catch (InterruptedException e) {
				// Never mind, just allow execution
			}
		}
		
		// fetch data (may block if wrapped PageFetcher blocks)
		HttpResponse response = fetcher.fetchPage(urlString);
		
		// save current time as last request time
		map.put(host, System.currentTimeMillis());
		
		return response;
	}
	
	private class StaleEntryDeleter implements Runnable {
		public void run() {
			while (running) {
				try {
					Thread.sleep(cleanupThreadSleep);
				} catch (InterruptedException e) {
					// never mind
				}
				
				long now = System.currentTimeMillis();

				for ( Map.Entry<String,Long> entry : map.entrySet() ) {
					if ( entry.getValue() != null && now - entry.getValue() > minRequestPeriod ) {
						map.remove( entry.getKey() );
					}
				}
			}
		}
	}
}
