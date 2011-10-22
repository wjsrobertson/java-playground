package net.xylophones.midifinder.util;

import junit.framework.TestCase;

public class UrlCleanerTest extends TestCase {
	
	UrlCleaner cleaner = new UrlCleaner();
		
	private void assertCleanPath(String expected, String parse) {
		System.out.println("dirty: " + parse);
		System.out.println("clean: " + cleaner.cleanPath(parse));
		System.out.println("----------------------------------");
		
		assertEquals(
			expected, cleaner.cleanPath(parse)
		);
	}
	
	public void testABunch() {
		assertCleanPath(
			"/banana.html", "/test/../banana.html"
		);
		assertCleanPath(
			"/banana.html", "/%20/../banana.html"
		);
		assertCleanPath(
			"/video_intros/index.htm", "/midi_files/../video_games/../video_intros/index.htm"
		);
		assertCleanPath(
			"/tv_news_archive/0903.htm", "/midi_files/../tv_news_archive/0903.htm"
		);	
		assertCleanPath(
			"/midi_files/tv_news_archive/0903.htm", "/midi_files/./tv_news_archive/0903.htm"
		);	
		assertCleanPath(
			"/midi_files/tv_news_archive/0903.htm", "/midi_files/./tv_news_archive/./0903.htm"
		);	
	}
	
	public void testStartDotDot() {
		assertCleanPath(
			"/banana.html", "/../banana.html"
		);
		assertCleanPath(
			"/ip-address/", "/../ip-address/"
		);
		assertCleanPath(
			"/img/com/pixel.gif", "/../img/com/pixel.gif"
		);
		assertCleanPath(
			"/search_tips.html", "/../search_tips.html"
		);
	}
	
	public void testNotBroken() {
		assertCleanPath(
			"/ed/", "/ed/"
		);
	}
}
