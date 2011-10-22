package org.xylophones.webscraper.http;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xylophones.webscraper.RobotsTxrFetcher;
import org.xylophones.webscraper.RobotsTxt;

public class RobotsTxtDecoratingPageFetcher implements PageFetcher {

	/**
	 * Logging instance
	 */
	private static final Log log = LogFactory.getLog(TidyDecoratingPageFetcher.class);
	
	private final PageFetcher fetcher;
	
	private final List<String> noCheckHosts = new ArrayList<String>();
	
	private final RobotsTxrFetcher robotsTxrFetcher;

	public RobotsTxtDecoratingPageFetcher(PageFetcher fetcher, RobotsTxrFetcher robotsTxrFetcher) {
		this.fetcher = fetcher;
		this.robotsTxrFetcher = robotsTxrFetcher;
	}
	
	@Override
	public HttpResponse fetchPage(String urlString) throws IOException {
		URL url     = new URL(urlString);
		String host = url.getHost();
		String path = url.getPath();
		String queryString = url.getQuery();
		
		String pathAndQueryString = path;
		if ( queryString != null ) {
			pathAndQueryString = pathAndQueryString + "?" + queryString;
		}

		if ( ! noCheckHosts.contains(host) ) {
			RobotsTxt robotsTxt = robotsTxrFetcher.getRobotsTxtParser(host);
			
			if ( robotsTxt.isAllowed(pathAndQueryString) ) {
				return fetcher.fetchPage(urlString);
			} else {
				return new HttpResponse(null, 403, "", null);
			}
		}
		
		return fetcher.fetchPage(urlString);
	}

}
