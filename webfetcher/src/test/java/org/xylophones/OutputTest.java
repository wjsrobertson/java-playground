package org.xylophones;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.xylophones.webscraper.Scraper;
import org.xylophones.webscraper.config.FilePageDefinitionLoader;
import org.xylophones.webscraper.parser.ParseComponent;
import org.xylophones.webscraper.parser.ParseException;
import org.xylophones.webscraper.parser.ParseResult;

public class OutputTest extends AbstractDependencyInjectionSpringContextTests {
	
	private static final Log log = LogFactory.getLog(OutputTest.class);
	
	private Scraper scraper;
	
	public void onSetUp() throws Exception {
		scraper = (Scraper) getContext(contextKey()).getBean("scraper");
	}

	public void testGen() throws IOException, ParseException {
		
		FilePageDefinitionLoader loader = new FilePageDefinitionLoader();
		loader.init("/home/poobar/eclipse-workspace/WebFetcher/src/test/resources/test.xml");
		
		Set<ParseComponent> parseComponents = loader.getPageComponents("www.altavista.com/video");
		String url = getUrl(2, "banana");
		
		ParseResult scrape = scraper.scrape(url, parseComponents.toArray(new ParseComponent[]{}));
		
		String total = scrape.getString("numresults");
		log.debug("total: " + total);
		
		List<String> hrefs = scrape.getList("result-hrefs");
		if ( hrefs != null ) {
			log.debug(hrefs.size() + " hrefs");
		}
		
		List<String> thumbnails = scrape.getList("result-thumbnails");
		if ( thumbnails != null ) {
			log.debug(thumbnails.size() + " thumbnails");
		}
		
		List<String> descriptions = scrape.getList("result-descriptions");
		if ( descriptions != null ) {
			log.debug(descriptions.size() + " descriptions");
		}
		
		List<String> information = scrape.getList("result-information-href");
		if ( information != null ) {
			log.debug(information.size() + " information");
		}
	}
	
	public void test2() throws IOException, ParseException {
		testGen() ;
	}
	
	/**
	 * 
	 * 
	 * @param pageNum
	 * @param query
	 * @return
	 */
	private String getUrl(int pageNum, String query) {
		return "http://localhost/fetcher/av.video.html";
		/*
		return "http://www.altavista.com/video/results?mvf=mpeg&q="
				+ query.replace(' ', '+') 
				+ "&stq=" + calculateStartNum(pageNum);
		*/
	}
	
	/**
	 * 
	 * 
	 * @param page
	 * @return
	 */
	private int calculateStartNum(int page) {
		return page * 20;
	}
	
	@Override
	public String[] getConfigLocations() {
		return new String[]{
			"classpath:applicationContext-webscraper-core.xml"
		};
	}
	
}
