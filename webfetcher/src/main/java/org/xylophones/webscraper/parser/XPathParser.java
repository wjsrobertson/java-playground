package org.xylophones.webscraper.parser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xylophones.webscraper.Scraper;

public class XPathParser implements Parser {
	
	/**
	 * Logging instance
	 */
	private static final Log log = LogFactory.getLog(XPathParser.class);
	
	private static final XPathFactory factory = XPathFactory.newInstance();
	
	static {
		 
	}

	/**
	 * {@inheritDoc}
	 */
	public void parse(byte[] data, ParseComponent[] parseComponents, ParseResult parseResult) throws ParseException {

		XPath xPath=factory.newXPath();

		for ( ParseComponent parseComponent : parseComponents ) {
			
			DataType type     = parseComponent.getDataType();
			String expression = parseComponent.getDefinition();
			String name       = parseComponent.getName();

			try {			
				InputSource inputSource = new InputSource( new ByteArrayInputStream(data) );
				inputSource.setEncoding("UTF-8");
				
				if ( type.equals(DataType.LIST) ) {
					NodeList nodes = (NodeList) xPath.evaluate(expression,inputSource, XPathConstants.NODESET);

					List<String> result = new ArrayList<String>();
					
					for (int i=0 ; i<nodes.getLength() ; i++) {
						Node item = nodes.item(i);
						result.add(item.getTextContent());
					}
					
					parseResult.putList(name, result);
				}
				
				if ( type.equals(DataType.STRING) ) {	
					Node node = (Node) xPath.evaluate(expression,inputSource, XPathConstants.NODE);
					if ( node != null ) {
						String result = node.getTextContent();
						parseResult.putString(name, result);
					}
				}

			} catch (XPathExpressionException e) {
				throw new ParseException(e);
			}
		}
	}
	
}
