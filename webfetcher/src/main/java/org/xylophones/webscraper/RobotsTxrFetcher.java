package org.xylophones.webscraper;

import java.io.IOException;

import org.xylophones.webscraper.http.HttpResponse;
import org.xylophones.webscraper.http.PageFetcher;

public class RobotsTxrFetcher {
	
	private final PageFetcher fetcher;
	
	private final String useragent;
	
	public RobotsTxrFetcher(PageFetcher fetcher, String useragent) {
		this.fetcher = fetcher;
		this.useragent = useragent;
	}

	public RobotsTxt getRobotsTxtParser(String hostname) throws IOException {

		RobotsTxt robotsTxtUtil = null;
		
		// fetch robots.txt file & check if request is valid
		HttpResponse robotsResponse = fetcher.fetchPage( "http://" + hostname + "/robots.txt");
		if ( robotsResponse.getStatusCode() == 200 ) {
			robotsTxtUtil = new RobotsTxt( robotsResponse.getData(), useragent );
		} else {
			robotsTxtUtil = new RobotsTxt( null, useragent );
		}
		
		return robotsTxtUtil;
	}
	
}
