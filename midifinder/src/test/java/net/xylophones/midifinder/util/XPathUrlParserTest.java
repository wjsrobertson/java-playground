package net.xylophones.midifinder.util;

import java.util.Map;

import junit.framework.TestCase;

public class XPathUrlParserTest extends TestCase {

	XPathUrlParser underTest = new XPathUrlParser();
	
	public void testAnchors() {
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<body>");
		html.append("<a href=\"banana.com\">I am a banana</a>");
		html.append("<frame src=\"apple.com\"></frame>");
		html.append("</body>");
		html.append("</html>");
		
		Map<String, String> parse = underTest.parse(html.toString().getBytes());
		assertEquals("I am a banana", parse.get("banana.com"));
		assertEquals("", parse.get("apple.com"));
	}
	
	public void testNormalize() {
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<body>");
		html.append("<a href=\"banana.com\">I am a banana\n\nwhoo  hoo\r\n\t    </a>");
		html.append("</body>");
		html.append("</html>");
		
		Map<String, String> parse = underTest.parse(html.toString().getBytes());
		assertEquals("I am a banana whoo hoo", parse.get("banana.com"));
	}
	
	public void testNonGreedyAnchorText() {
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<body>");
		html.append("<a href=\"banana.com\">I am a banana</a><a href=\"apple.com\">I am an apple</a>");
		html.append("</body>");
		html.append("</html>");
		
		Map<String, String> parse = underTest.parse(html.toString().getBytes());
		assertEquals("I am a banana", parse.get("banana.com"));
		assertEquals("I am an apple", parse.get("apple.com"));
	}
}
