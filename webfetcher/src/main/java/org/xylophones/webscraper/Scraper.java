package org.xylophones.webscraper;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xylophones.webscraper.http.PageFetcher;
import org.xylophones.webscraper.parser.ParseComponent;
import org.xylophones.webscraper.parser.ParseException;
import org.xylophones.webscraper.parser.ParseResult;
import org.xylophones.webscraper.parser.Parser;

public class Scraper {
	
	/**
	 * Logging instance
	 */
	private static final Log log = LogFactory.getLog(Scraper.class);
	
	private final PageFetcher fetcher;
	
	private final Parser parser;

	public Scraper(PageFetcher fetcher, Parser parser) {
		this.fetcher   = fetcher;
		this.parser    = parser;
	}

	public ParseResult scrape(String url, ParseComponent[] parseComponents) throws IOException, ParseException {
		byte[] data = fetcher.fetchPage(url).getData();
		ParseResult parseResult = new ParseResult();

		if (log.isDebugEnabled()) {
			String string = new String(data);
			int printLength = 1024;
			
			if ( string.length() > printLength ) {
				String substring = string.substring(0, 1024);
				log.debug("About to parse HTML: \n" + substring);
			} else {
				log.debug("About to parse HTML: \n" + string);
			}
		}

		parser.parse(data, parseComponents, parseResult);
		
		return parseResult;
	}

}
