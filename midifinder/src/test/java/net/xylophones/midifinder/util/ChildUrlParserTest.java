package net.xylophones.midifinder.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import junit.framework.TestCase;
import net.xylophones.midifinder.model.Url;

public class ChildUrlParserTest extends TestCase {
	
	private ChildUrlParser underTest;
	
	private HtmlTidier htmlTidier;
	
	private XPathUrlParser urlParser;
	
	private UrlCleaner urlCleaner;
	
	public void setUp() {
		htmlTidier = mock(JTidyHtmlTidier.class);
		urlParser  = mock(XPathUrlParser.class);
		urlCleaner  = mock(UrlCleaner.class);
		underTest  = new ChildUrlParser(htmlTidier, urlParser, urlCleaner);
	}
	
	/*
	public void testX() {
		byte[] data = new byte[]{};
		when( htmlTidier.tidy(data) ).thenReturn(data);

		String[] urls = {
			"mailto:fuckoff",
			"#banana.txt",
			"banana.txt",
			"relative/banana.txt",
			"/root/fruit/banana.txt",
			"http://banana.com/banana.txt"
		};		
		when( urlParser.parse(data) ).thenReturn(urls);
		
		Url referer = new Url();
		referer.setHost("www.apple.com");
		referer.setPath("/appleroot/");
		referer.setScheme("https");
		
		Url[] childUrls = underTest.createChildUrls(data, referer);
		assertEquals(4, childUrls.length);
		
		for (Url url: childUrls) {
			System.out.println(url);			
		}
	}
	
	public void testY() {

		byte[] data = new byte[]{};
		when( htmlTidier.tidy(data) ).thenReturn(data);

		String[] urls = {
			"banana.txt",
			"relative/banana.txt",
			"/root/fruit/banana.txt",
			"http://banana.com/banana.txt"
		};		
		when( urlParser.parse(data) ).thenReturn(urls);
		
		Url referer = new Url();
		referer.setHost("www.apple.com");
		referer.setPath("/");
		referer.setScheme("https");
		
		Url[] childUrls = underTest.createChildUrls(data, referer);
		assertEquals(4, childUrls.length);
		
		for (Url url: childUrls) {
			System.out.println(url);			
		}
	}
	
	public void testZ() {

		byte[] data = new byte[]{};
		when( htmlTidier.tidy(data) ).thenReturn(data);

		String[] urls = {
			"banana.txt",
			"relative/banana.txt",
			"/root/fruit/banana.txt",
			"http://banana.com/banana.txt"
		};		
		when( urlParser.parse(data) ).thenReturn(urls);
		
		Url referer = new Url();
		referer.setHost("www.apple.com");
		referer.setPath("/myFile.txt");
		referer.setScheme("https");
		
		Url[] childUrls = underTest.createChildUrls(data, referer);
		assertEquals(4, childUrls.length);
		
		for (Url url: childUrls) {
			System.out.println(url);			
		}
	}
	
	public void testA() {

		byte[] data = new byte[]{};
		when( htmlTidier.tidy(data) ).thenReturn(data);

		String[] urls = {
			"banana.txt",
			"relative/banana.txt",
			"/root/fruit/banana.txt",
			"http://banana.com/banana.txt"
		};		
		when( urlParser.parse(data) ).thenReturn(urls);
		
		Url referer = new Url();
		referer.setHost("www.apple.com");
		referer.setPath("/sub/sub2/");
		referer.setScheme("https");
		
		Url[] childUrls = underTest.createChildUrls(data, referer);
		assertEquals(4, childUrls.length);
		
		for (Url url: childUrls) {
			System.out.println(url);			
		}
	}
	*/
	
	/*
	public void testPurpleGuppy() {
		byte[] data = new byte[]{};
		when( htmlTidier.tidy(data) ).thenReturn(data);

		String[] urls = {
			"Alluro95.mid",
			"/midimp3/Archive/Alluro95.mp3"
		};		
		when( urlParser.parse(data) ).thenReturn(urls);
		
		Url referer = new Url();
		referer.setHost("www.purpleguppy.net");
		referer.setPath("/alluro95/main.html");
		referer.setScheme("http");
		
		Url[] childUrls = underTest.createChildUrls(data, referer);
		
		Url[] expected = {
			new Url("http", "www.purpleguppy.net", null, "/alluro95/Alluro95.mid", null),
			new Url("http", "www.purpleguppy.net", null, "/midimp3/Archive/Alluro95.mp3", null)
		};
		
		System.out.println( Arrays.toString(childUrls) );
		
		assertTrue( Arrays.equals(expected, childUrls) );
	}	
	
	public void testHostOnly() {
		byte[] data = new byte[]{};
		when( htmlTidier.tidy(data) ).thenReturn(data);

		String[] urls = {
			"http://www.example.com",
		};		
		when( urlParser.parse(data) ).thenReturn(urls);
		
		Url referer = new Url();
		referer.setHost("www.purpleguppy.net");
		referer.setPath("/main.html");
		referer.setScheme("http");
		
		Url[] childUrls = underTest.createChildUrls(data, referer);
		
		Url[] expected = {
			new Url("http", "www.example.com", null, "/", null),
		};
		
		System.out.println( Arrays.toString(childUrls) );
		
		assertTrue( Arrays.equals(expected, childUrls) );
	}
	*/
}
