package net.xylophones.midifinder.util;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XPathUrlParser {
	
	private static final Logger log = Logger.getLogger(XPathUrlParser.class);

	private static final XPathFactory factory = XPathFactory.newInstance();
	
	private static final String[] expressions = new String[]{
		"//frame/@src",
		"//embed/@src",
		"//iframe/@src"
	};
	// 		,
	/**
	 * Get child URLs from a page
	 * 
	 * @param data
	 * @return
	 */
	public Map<String,String> parse(byte[] data) {
		Map<String,String> urlStrings = new HashMap<String,String>();
		
		XPath xPath=factory.newXPath();
				
		for (String expression: expressions) {
			InputSource inputSource = new InputSource( new ByteArrayInputStream(data) );
			
			inputSource.setEncoding("UTF-8");
			NodeList nodes;
			
			try {
				nodes = (NodeList) xPath.evaluate(expression,inputSource, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				log.warn("Error executing: " + expression + ", " + e.getMessage());
				continue;
			}
			
			for (int i=0 ; i<nodes.getLength() ; i++) {
				Node item = nodes.item(i);
				urlStrings.put(item.getTextContent().trim(), "");
			}
		}
		
		/*
		 * Anchors (with text)
		 */
		
		InputSource inputSource = new InputSource( new ByteArrayInputStream(data) );
		inputSource.setEncoding("UTF-8");
		NodeList nodes = null;
		try {
			nodes = (NodeList) xPath.evaluate("//a",inputSource, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			log.warn("Error executing anchors: " + "//a" + ", " + e.getMessage());
		}
		
		if (nodes != null) {
			for (int i=0 ; i<nodes.getLength() ; i++) {
				Node item = nodes.item(i);
				Node href = null;
				try {
					href = (Node) xPath.evaluate("@href",item, XPathConstants.NODE);
				} catch (XPathExpressionException e) {
					log.warn("Error executing anchors: " + "//a" + ", " + e.getMessage());
				}
				if (href != null) {
					urlStrings.put(
						href.getTextContent().trim(), 
						normalizeText(item.getTextContent().trim())
					);						
				}
			}
		}
		
		return urlStrings;
	}
	
	private String normalizeText(String text) {
		return text.replaceAll("\\s+", " ");
	}
}

/*
StringBuffer buffer = new StringBuffer();
buffer.append("<html><body>");
buffer.append("<a href=\"banana.txt\">banana</a>");
buffer.append("<a href=\"http://banana.com/banana.txt\">test1</a>");
buffer.append("<a href=\"fruit/banana.txt\">test2</a>");
buffer.append("<a href=\"/banana/fruit/banana.txt\">test3</a>");
buffer.append("</body></html>");

byte[] data = buffer.toString().getBytes();
*/