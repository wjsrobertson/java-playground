package net.xylophones.midifinder.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("intervalRetriever")
public class IntervalDecoratingRetriever implements Retriever {

	private static final Logger log = Logger.getLogger(IntervalDecoratingRetriever.class);
	
	/**
	 * host -> request time mapping 
	 */
	private final ConcurrentHashMap<String,Long> map = new ConcurrentHashMap<>();
	
	private final long minRequestPeriod; 
	
	private final Retriever retriever;
	
	private volatile boolean running = true;
	
	private final long cleanupThreadSleep = 30000;	// every half minute 
	
	private final StaleEntryDeleter staleEntryDeleter;
	
	@Autowired
	public IntervalDecoratingRetriever(@Qualifier("retriever") Retriever retriever) {
		this.retriever = retriever;
		this.minRequestPeriod = 1000;
		
		staleEntryDeleter = new StaleEntryDeleter();
		Thread thread = new Thread(staleEntryDeleter);
		thread.start();
	}
	
	public IntervalDecoratingRetriever(Retriever retriever, long minRequestPeriod) {
		this.retriever = retriever;
		this.minRequestPeriod = minRequestPeriod;
		
		staleEntryDeleter = new StaleEntryDeleter();
		Thread thread = new Thread(staleEntryDeleter);
		thread.start();
	}
	
	@Override
	public byte[] getBody(String url) throws IOException {
		blockIfRequired(url);
		return retriever.getBody(url);
	}

	@Override
	public HeadMethod getHead(String url) throws IOException {
		blockIfRequired(url);
		return retriever.getHead(url);
	}
	
	private void blockIfRequired(String urlString) {
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		String host = url.getHost();
		
		// calculate wait time (if any)
		long requiredWait = -1;
		Long previousRequestTime = map.get(host);
		if (previousRequestTime != null) {
			requiredWait = previousRequestTime - System.currentTimeMillis() + minRequestPeriod;
		}
		
		// block & wait if required
		if ( requiredWait > 0 ) {
			try {
				if (log.isDebugEnabled()) {
					log.debug("Sleeping for " + requiredWait + "ms on: " + urlString);				
				}
				Thread.sleep(requiredWait);
				if (log.isDebugEnabled()) {
					log.debug("Continuing with: " + urlString);
				}
			} catch (InterruptedException e) {
				// Never mind, just allow execution
			}
		}

		// save current time as last request time
		map.put(host, System.currentTimeMillis());
	}
	
	/**
	 * 
	 */
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
