package net.xylophones.midifinder.runner;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.xylophones.midifinder.http.Retriever;
import net.xylophones.midifinder.model.Url;
import net.xylophones.midifinder.model.UrlStatus;
import net.xylophones.midifinder.service.jpa.JpaUrlService;
import net.xylophones.midifinder.util.ChildUrlParser;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

public class UrlProcessor {
	
	private static final Logger log = Logger.getLogger(UrlProcessor.class);
	
	private int batchSize = 50;
	
	private int preferredBatchSize = 100;

	private int noUrlsWaitMs = 2000;

	private final ExecutorService retrieveHeadersService;
	
	private final ExecutorService retrieveChildrenService;

	private final JpaUrlService jpaUrlService;
	
	private final Retriever retriever;
	
	private final ChildUrlParser childUrlParser;
	
	private volatile boolean running = true;

	public UrlProcessor(
			JpaUrlService jpaUrlService,
			Retriever retriever,
			ChildUrlParser childUrlParser,
			int numUrlParseThreads,
			int numUrlHeadThreads,
			int pageChildQueueSize,
			int urlHeadQueueSize) {
		
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
		
		if (numUrlHeadThreads > 0) {
			retrieveHeadersService = new ThreadPoolExecutor(
				numUrlHeadThreads, 
				numUrlHeadThreads,
	            0L, TimeUnit.MILLISECONDS,
	            new ArrayBlockingQueue<Runnable>(urlHeadQueueSize),
	            new ThreadPoolExecutor.CallerRunsPolicy()
	        );
		} else {
			retrieveHeadersService = null;
		}
	}
	
	public void shutdown() {
		/*
		log.debug("Shutting down url processor");
		
		List<Runnable> shutdownNow = retrieveHeadersService.shutdownNow();
		// TODO - release all runnables URLs

		// release all URLs pending in list
		for (Url url: fetchHeaderUrls) {
			release(url);
		}
		*/
	}
    
	public void init() {
		log.info("URL Processor started");
		Thread.currentThread().setName("Main thread");
		
		/*
		 * Kick off thread adding URLs to the blocking queue
		 */
		Thread requestHeadersProducer = new Thread( 
			new Runnable() {
				public void run() {
					try {
						while (running) {
							log.debug("Retrieving urls to add to queue");
							
							List<Url> retrieve = jpaUrlService.getAndLockPreferred(preferredBatchSize, null, UrlStatus.NEW);
							List<Url> secondary = jpaUrlService.getAndLock(batchSize, null, UrlStatus.NEW);
							retrieve.addAll(secondary);
							
							if (retrieve.size() > 0) {
								log.info("Retrieved " + retrieve.size() + " URLs to retrieve headers");
								
								for (Url url : retrieve) {
									log.info("Submitting URL to get headers: " + url);
									retrieveHeadersService.submit( new UrlHeadersTask(url) );
								}
							} else {
								try {
									log.debug("No urls retrieved for fetching headers - waiting " + noUrlsWaitMs + " until next check" );
									Thread.sleep(noUrlsWaitMs);
								} catch (InterruptedException e) {
									// never mind
								}
							}
						}
					} catch (Exception e) {
						log.error("Error adding urls to queue", e);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
						}
					}
				}
			},
			"URL response header producer"
		);
		if (retrieveHeadersService != null) {
			requestHeadersProducer.start();			
		}
		
		/*
		 * Kick off thread adding URLs to the blocking queue
		 */
		Thread retrieveChildUrlsProducer = new Thread( 
			new Runnable() {
				public void run() {
					while (running) {
						log.debug("Retrieving urls to add to queue of HTML pages to parse");
						
						try {
							
							List<Url> retrieve = jpaUrlService.getAndLockPreferred(batchSize, "text/html",UrlStatus.HEADERS_RETRIEVED);
							List<Url> secondary = jpaUrlService.getAndLockPreferred(batchSize, "text/html",UrlStatus.HEADERS_RETRIEVED);
							retrieve.addAll(secondary);
							
							if (retrieve.size() > 0) {
								log.info("Retrieved " + retrieve.size()+ " URLs from DB to GET and parse");

								for (Url url : retrieve) {
									log.info("Submitting URL to get children: "+ url);
									retrieveChildrenService.submit(new UrlChildrenTask(url));
								}
								log.debug("All urls added to queue for GET and parse");
							} else {
								try {
									log.debug("No urls retrieved for parsing - waiting "+ noUrlsWaitMs+ " until next check");
									Thread.sleep(noUrlsWaitMs);
								} catch (InterruptedException e) {
									// never mind
								}
							}
						} catch (Exception e) {
							log.error("Error retrieving URLs to parse", e);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
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
		
		log.debug("Exiting init method");
	}
	
	private void release(Url url) {
		url.setLockedDate(null);
		jpaUrlService.update(url);
	}
	
	/**
	 * Task to fetch child URLs from a HTML page
	 */
	private class UrlChildrenTask implements Callable<Boolean> {
		
		private final Url url;
		
		public UrlChildrenTask(Url url) {
			this.url = url;
		}
		
		@Override
		public Boolean call() throws Exception {
			try {
				log.debug("Starting process of fetching children of '"+url+"'");
				
				byte[] body = null;
				try {
					body = retriever.getBody(url.getFullUrl());
				} catch (IOException e) {
					markFailed(url);
					return false;
				}

                Set<Url> childUrls = childUrlParser.createChildUrls(body, url);
				saveUrlsAndMarkRetrieved(childUrls, url);
				
				log.debug(childUrls.size() + " children of '"+url+"' found");
				return true;
			} catch (Exception e) {
				log.warn("Fetching children of '"+url+"' failed - " + e.getMessage(), e);
				markFailed(url);
				return false;
			}
		}
		
		@Transactional
		private void markFailed(Url url) {
			url.setStatus(UrlStatus.REQUEST_FAILED);
			url.setLastRequestDate(new Date());
			url.setLockedDate(null);
			jpaUrlService.update(url);
		}
		
		@Transactional
		private void saveUrlsAndMarkRetrieved(Set<Url> childUrls, Url url) {
			final int parentDepth = url.getDepth() == null ? 0: url.getDepth();
			final int childDepth = parentDepth+1;
			for (Url childUrl: childUrls) {
				
				childUrl.setDepth(childDepth);
				try {
					if (! jpaUrlService.exists(childUrl.getHost(), childUrl.getPath(), childUrl.getQuery()) ) {
						jpaUrlService.save(childUrl);					
					} else {
						log.info("Not saving url since it is saved already: " + childUrl);
					}
				} catch (RuntimeException e) {
					log.warn("Couldn't save url: '" + childUrl + "' - " + e.getMessage());
				}
			}
			
			url.setStatus(UrlStatus.RETRIEVED);
			url.setLastRequestDate(new Date());
			url.setLockedDate(null);
			jpaUrlService.update(url);
		}
	}
	
	/**
	 * Task to retrieve a URLs headers
	 */
	private class UrlHeadersTask implements Callable<Boolean> {
		
		private final Url url;
		
		public UrlHeadersTask(Url url) {
			this.url = url;
		}
		
		@Override
		public Boolean call() throws Exception {
			try {
				log.debug("Starting process of retriving headers of '"+url+"'");
				
				HeadMethod head = null;
				try {
					head = retriever.getHead(url.getFullUrl());
				} catch (IOException e) {
					markFailed(url);
					return false;
				}
				
				String contentType = null;
				Header contentTypeHeader = head.getResponseHeader("Content-Type");
				if (contentTypeHeader != null) {
					String value = contentTypeHeader.getValue();
					if (value != null) {
						contentType = value;
					}
				}
				if (contentType!= null && contentType.contains(";")) {
					contentType = contentType.substring( 0, contentType.indexOf(";") );
				}
				
				Long contentLength = null;
				Header contentLengthHeader = head.getResponseHeader("Content-Length");
				if (contentLengthHeader != null) {
					String value = contentLengthHeader.getValue();
					if (value != null) {
						contentLength = Long.valueOf(value);
					}
				}
				if (contentLength != null) {
					url.setDataSize( (int) (long) contentLength);				
				}
				url.setMimeType(contentType);
				url.setStatusCode(head.getStatusCode());
				
				markRetrievedHeader(url);
				
				log.debug("Retrieving headers of '"+url+"' complete" );
				return true;
			} catch (Exception e) {
				log.warn("Fetching children of '"+url+"' failed - " + e.getMessage(), e);
				markFailed(url);
				return false;
			}
		}
		
		@Transactional
		private void markRetrievedHeader(Url url) {
			url.setLastRequestDate(new Date());
			url.setLockedDate(null);
			url.setStatus(UrlStatus.HEADERS_RETRIEVED);
			jpaUrlService.update(url);
		}
		
		@Transactional
		private void markFailed(Url url) {
			url.setStatus(UrlStatus.REQUEST_FAILED);
			url.setLastRequestDate(new Date());
			url.setLockedDate(null);
			jpaUrlService.update(url);
		}
	}
}
