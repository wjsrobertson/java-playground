package net.xylophones.midifinder.runner;

import net.xylophones.midifinder.http.Retriever;
import net.xylophones.midifinder.model.Url;
import net.xylophones.midifinder.model.UrlStatus;
import net.xylophones.midifinder.service.jpa.JpaUrlService;
import net.xylophones.midifinder.util.ChildUrlParser;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class HtmlUrlProcessor {

	private static final Logger log = Logger.getLogger(HtmlUrlProcessor.class);

	private final int batchSize = 50;

	private final int noUrlsWaitMs = 2000;

	private final ExecutorService retrieveChildrenService;

	private final JpaUrlService jpaUrlService;

	private final Retriever retriever;

	private final ChildUrlParser childUrlParser;

	private volatile boolean running = true;

	public HtmlUrlProcessor(
            JpaUrlService jpaUrlService,
            Retriever retriever,
            ChildUrlParser childUrlParser,
            int numUrlParseThreads,
            int pageChildQueueSize) {
		
		this.jpaUrlService = jpaUrlService; 
		this.retriever = retriever;
		this.childUrlParser = childUrlParser;
		
		if (numUrlParseThreads > 0) {
			retrieveChildrenService = new ThreadPoolExecutor(
				numUrlParseThreads, 
				numUrlParseThreads,
	            0L, TimeUnit.MILLISECONDS,
	            new ArrayBlockingQueue<Runnable>(pageChildQueueSize),
	            new ThreadPoolExecutor.CallerRunsPolicy()
		    );
		} else {
			retrieveChildrenService = null;
		}
	}
	
	public void shutdown() {
		log.debug("Shutting down url processor");
        
        running = false;
		
		List<Runnable> shutdownNow = retrieveChildrenService.shutdownNow();

		// TODO - release all runnables URLs

        /*
		// release all URLs pending in list
		for (Url url: fetchHeaderUrls) {
			jpaUrlService.releaseDbLock(url);
		}
		*/
	}
    
	public void startInNewThread() {
		log.info("URL Processor starting");

		Thread retrieveChildUrlsProducer = new Thread( 
			new Runnable() {
				public void run() {
					while (running) {
						log.debug("Retrieving urls to add to queue of HTML pages to parse");

						try {
							List<Url> retrieved = jpaUrlService.getAndLock(batchSize, "text/html", UrlStatus.NEW);

							if (retrieved.size() > 0) {
								log.info("Retrieved " + retrieved.size() + " URLs from DB to GET and parse");

								for (Url url : retrieved) {
									log.info("Submitting URL to get children: "+ url);
									retrieveChildrenService.submit(new HtmlUrlChildrenTask(jpaUrlService, retriever, childUrlParser, url));
								}
								log.debug("All urls added to queue for GET and parse");
							} else {
								try {
									log.debug("No urls retrieved for parsing - waiting " + noUrlsWaitMs + " until next check");
									Thread.sleep(noUrlsWaitMs);
								} catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
								}
							}
						} catch (Exception e) {
							log.error("Error retrieving URLs to parse", e);

							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
                                Thread.currentThread().interrupt();
							}
						}
					}				
				}
			},
			"HTML body producer"
		);
		if (retrieveChildrenService != null) {
			retrieveChildUrlsProducer.start();
		}

		log.info("URL Processor started");
	}
}
