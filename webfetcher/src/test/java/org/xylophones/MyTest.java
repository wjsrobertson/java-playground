package org.xylophones;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.xylophones.webscraper.Scraper;
import org.xylophones.webscraper.parser.DataType;
import org.xylophones.webscraper.parser.ParseComponent;
import org.xylophones.webscraper.parser.ParseResult;

public class MyTest extends AbstractDependencyInjectionSpringContextTests {
	
	private static final Log log = LogFactory.getLog(MyTest.class);
	
	private Scraper scraper;
	
	public void onSetUp() throws Exception {
		scraper = (Scraper) getContext(contextKey()).getBean("scraper");
	}

	/*
	@Test
	public void testFetchMinimal() throws Exception {
		String url = "http://localhost/fetcher/minimal.html";

		ParseComponent[] parseComponents = new ParseComponent[] {
			new ParseComponent(
				"href",
				"/html/body/div/span",
				 DataType.LIST
			)
		};

		ParseResult parseResult = scraper.scrape(url, parseComponents);
		log.info("Parse result: " + parseResult);
		
		assertNotNull( parseResult.getList("href") );
		assertTrue( parseResult.getList("href").size() > 0 );
	}
	*/
	
	/*
	@Test
	public void testGoogle() throws Exception {
		String url = "http://localhost/fetcher/google.html";

		ParseComponent[] parseComponents = new ParseComponent[] {
			new ParseComponent(
				"result-hrefs",
				"//div[@class='g']/h2[@class='r']/a/@href",
				 DataType.LIST
			),
			new ParseComponent(
				"result-anchortexts",
				"//div[@class='g']/h2[@class='r']/a",
				 DataType.LIST
			),
			new ParseComponent(
				"result-descriptions",
				//"//div[@class='g']/table/tr/td/div[@class='std']/child::*[not(span) and not(a)]",
				//"//div[@class='g']/table/tr/td/descendant::*[not(span) and not(a)]",
				//"//div[@class='g']/table/tr/td/div[@class='std']/child::text()[1]",
				"//div[@class='g']/table/tr/td/div[@class='std']/descendant::text()[1]", // [name()!='span' and name()!='a' and name()!='nobr']
				 DataType.LIST
			),
			new ParseComponent(
				"numresults",
				"//table[@class='t bt']/tr/td/b[3]",
				 DataType.STRING
			),
		};
		
		// child::*[position()=1]
		
		ParseResult parseResult = scraper.scrape(url, parseComponents);
		log.info("Parse result: " + parseResult);
		
		assertNotNull( parseResult.getList("result-hrefs") );
		assertTrue( parseResult.getList("result-hrefs").size() > 0 );
	}
	*/
	

	@Test
	public void testYahoo() throws Exception {
		String url = "http://localhost/fetcher/yahoo.html";
		
		ParseComponent[] parseComponents = new ParseComponent[] {
			new ParseComponent(
				"result-hrefs",
				"//div[@id='web']//h3",
				 DataType.LIST
			),
			new ParseComponent(
				"result-anchortexts",
				"//div[@id='web']//h3/a/@href",
				 DataType.LIST
			),
			new ParseComponent(
				"result-descriptions",
				"//div[@id='web']//div[@class='abstr']",
				 DataType.LIST
			),
			new ParseComponent(
				"numresults",
				"//div[@id='info']/p[2]/child::text()[1]",
				 DataType.STRING
			),
		};

		ParseResult parseResult = scraper.scrape(url, parseComponents);
		log.info("Parse result: " + parseResult);
		
		assertNotNull( parseResult.getList("result-hrefs") );
		assertTrue( parseResult.getList("result-hrefs").size() > 0 );
	}

	
	/*
	@Test
	public void testAsk() throws Exception {
		String url = "http://localhost/fetcher/ask.html";
		
		ParseComponent[] parseComponents = new ParseComponent[] {
			new ParseComponent(
				"result-hrefs",
				"//div[@id='teoma-results']/div//table/tr/td[2]/a/@href",
				 DataType.LIST
			),
			new ParseComponent(
				"result-anchortexts",
				"//div[@id='teoma-results']/div//table/tr/td[2]/a[@class='L4']",
				 DataType.LIST
			),
			new ParseComponent(
				"result-descriptions",
				"//div[@id='teoma-results']//div[@class='T1']",
				 DataType.LIST
			),
			new ParseComponent(
				"numresults",
				"//div[@id='header']//span[@class='T7']",
				 DataType.STRING
			),
		};

		ParseResult parseResult = scraper.scrape(url, parseComponents);
		log.info("Parse result: " + parseResult);
		
		assertNotNull( parseResult.getList("result-hrefs") );
		assertTrue( parseResult.getList("result-hrefs").size() > 0 );
	}
	*/
	
	/*
	@Test
	public void testMsn() throws Exception {
		String url = "http://localhost/fetcher/msn.html";
		
		ParseComponent[] parseComponents = new ParseComponent[] {
			new ParseComponent(
				"result-hrefs",
				"//xml:div",
				 DataType.LIST
			),
			new ParseComponent(
				"result-anchortexts",
				"//web:ul[@class='sb_results']/li/h3/a",
				 DataType.LIST
			),
			new ParseComponent(
				"result-descriptions",
				"//web:ul[@class='sb_results']/li/p[1]",
				 DataType.LIST
			),
			new ParseComponent(
				"numresults",
				"//web:span[@id='YNF_F']",
				 DataType.STRING
			),
		};
		
		ParseResult parseResult = scraper.scrape(url, parseComponents);
		log.info("Parse result: " + parseResult);
		
		assertNotNull( parseResult.getList("result-hrefs") );
		assertTrue( parseResult.getList("result-hrefs").size() > 0 );
	}
	*/
	
	/*
	@Test
	public void testAltavista() throws Exception {
		String url = "http://localhost/fetcher/av2.html";
		
		ParseComponent[] parseComponents = new ParseComponent[] {
			new ParseComponent(
				"result-hrefs",
				"//br[@class='smbr']/following-sibling::br[@class='lb']/following-sibling::a[@class='res']",
				 DataType.LIST
			),
			new ParseComponent(
				"result-anchortexts",
				"//br[@class='smbr']/following-sibling::br[@class='lb']/following-sibling::a[@class='res']/@href",
				 DataType.LIST
			),
			new ParseComponent(
				"result-descriptions",
				"//br[@class='smbr']/following-sibling::br[@class='lb']/following-sibling::span[@class='s']",
				 DataType.LIST
			),
		};
		
		ParseResult parseResult = scraper.scrape(url, parseComponents);
		log.debug("Parse result: " + parseResult);
		
		assertNotNull( parseResult.getList("result-hrefs") );
		assertTrue( parseResult.getList("result-hrefs").size() > 0 );
	}
	*/
	
	/*
	@Test
	public void testAltavistaVideo() throws Exception {
		String url = "http://localhost/fetcher/av.video.html";
		
		ParseComponent[] parseComponents = new ParseComponent[] {
			new ParseComponent(
				"result-hrefs",
				"//tr[@class='resultGroup']//a[@class='thumbnail']/@href",
				 DataType.LIST
			),
			new ParseComponent(
				"result-thumbnails",
				"//tr[@class='resultGroup']//img[@class='thumbnail']/@src",
				 DataType.LIST
			),
			new ParseComponent(
				"result-descriptions",
				"//tr[@class='resultGroup']//b",
				 DataType.LIST
			),
			new ParseComponent(
				"result-durations",
				"//tr[@class='resultGroup']//span[@class='resSize']/text()[1]",
				 DataType.LIST
			),

			new ParseComponent(
				"numresults",
				"//td[@colspan='4']/div[@class='xs']/b[@class='lbl']",
				 DataType.STRING
			),
		};
		
		ParseResult parseResult = scraper.scrape(url, parseComponents);
		log.debug("Parse result: " + parseResult);
		
		assertNotNull( parseResult.getList("result-hrefs") );
		assertTrue( parseResult.getList("result-hrefs").size() > 0 );
	}
	*/
	
	/*
	@Test
	public void testAltavistaImage() throws Exception {
		String url = "http://localhost/fetcher/av.image.html";
		
		ParseComponent[] parseComponents = new ParseComponent[] {
			new ParseComponent(
				"result-hrefs",
				"//tr[@class='resultGroup']//a[@class='thumbnail']/@href",
				 DataType.LIST
			),
			new ParseComponent(
				"result-thumbnails",
				"//tr[@class='resultGroup']//img[@class='thumbnail']/@src",
				 DataType.LIST
			),
			new ParseComponent(
				"result-descriptions",
				"//tr[@class='resultGroup']//b",
				 DataType.LIST
			),
			new ParseComponent(
				"result-filesize",
				"//tr[@class='resultGroup']//span[@class='resSize']/text()[1]",
				 DataType.LIST
			),
			new ParseComponent(
				"result-file-dimentions",
				"//tr[@class='resultGroup']//span[@class='resSize']/text()[1]",
				 DataType.LIST
			),
			new ParseComponent(
				"result-information-href",
				"//span[@class='resSize']/a/@href",
				 DataType.LIST
			),
			new ParseComponent(
				"numresults",
				"//td[@colspan='4']/div[@class='xs']/b[@class='lbl']",
				 DataType.STRING
			),
		};
		
		ParseResult parseResult = scraper.scrape(url, parseComponents);
		log.debug("Parse result: " + parseResult);
		
		assertNotNull( parseResult.getList("result-hrefs") );
		assertTrue( parseResult.getList("result-hrefs").size() > 0 );
	}
	*/
	

	@Override
	public String[] getConfigLocations() {
		return new String[]{
			"classpath:applicationContext-webscraper-core.xml"
		};
	}

}
