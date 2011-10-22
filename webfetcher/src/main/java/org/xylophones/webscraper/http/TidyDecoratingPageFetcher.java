package org.xylophones.webscraper.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.tidy.Tidy;

public class TidyDecoratingPageFetcher implements PageFetcher {
	
	/**
	 * Logging instance
	 */
	private static final Log log = LogFactory.getLog(TidyDecoratingPageFetcher.class);

	private final PageFetcher fetcher;
	
	public TidyDecoratingPageFetcher(PageFetcher fetcher) {
		this.fetcher = fetcher;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public HttpResponse fetchPage(String url) throws IOException {
		HttpResponse response = fetcher.fetchPage(url);
		byte[] responseBody = response.getData();
		
		if (log.isDebugEnabled()) {
			log.debug("Original HTML ("+responseBody.length+" bytes): \n" + new String(responseBody) );
		}
		
		Tidy tidy = new Tidy();
		tidy.setTabsize(4);
		//tidy.setBreakBeforeBR(true);
		//tidy.setIndentAttributes(true);
		tidy.setQuiet(true);
		tidy.setIndentContent(true);
		//tidy.setOnlyErrors(true);
		tidy.setMakeClean(true);
		//tidy.setMakeBare(true);
		//tidy.setWord2000(true);
		tidy.setXmlOut(true);
		tidy.setXmlSpace(true);
		tidy.setForceOutput(true);
		tidy.setXmlTags(false);
		tidy.setDocType("omit");
		//tidy.setPrintBodyOnly(true);
		//tidy.setXHTML(false);
		//tidy.setFixUri(true);
		ByteArrayInputStream in = new ByteArrayInputStream(responseBody);
		ByteArrayOutputStream out = new ByteArrayOutputStream();	
		tidy.parse(in, out);
		byte[] tidyHtml = out.toByteArray();
		
		if (log.isDebugEnabled()) {
			log.debug("Generated tidied XHTML ("+tidyHtml.length+" bytes): \n" + new String(tidyHtml) );
		}
		
		HttpResponse tidyResponse = new HttpResponse(
			tidyHtml,
			response.getStatusCode(),
			response.getStatusString(),
			response.getSimpleHeaders()
		);
		
		return tidyResponse;
	}
	
}
