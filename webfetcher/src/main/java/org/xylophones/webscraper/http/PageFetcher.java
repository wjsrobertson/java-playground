package org.xylophones.webscraper.http;

import java.io.IOException;

public interface PageFetcher {
	
	public HttpResponse fetchPage(String url) throws IOException;

}
