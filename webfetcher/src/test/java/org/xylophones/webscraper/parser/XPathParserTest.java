package org.xylophones.webscraper.parser;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class XPathParserTest extends TestCase {

	/**
	 * Logging instance
	 */
	private static final Log log = LogFactory.getLog(XPathParserTest.class);

	private XPathParser xPathParser = new XPathParser();
	
	public void testXPathParser() throws ParseException {
		
		String xhtml =  "	<html>" + 
						"	  <head>" + 
						"	    <meta name=\"generator\" content=\"HTML Tidy, see www.w3.org\" />" + 
						"	    <title>" + 
						"	    </title>" + 
						"	  </head>" + 
						"	  <body>" + 
						"	    <div>" + 
						"	      <span>banana site</span> <a" + 
						"	      href=\"http://fruit.com\">banana</a>" + 
						"	    </div>" + 
						"	    <div>" + 
						"	      <span>banana site 2</span> <a" + 
						"	      href=\"http://fruit2.com\">banana2</a>" + 
						"	    </div>" + 
						"	  </body>" + 
						"	</html>";
		
		ParseComponent[] parseComponents = new ParseComponent[] {
			new ParseComponent(
				"href",
				"/html/body/div/span",
				 DataType.LIST
			)
		};
		
		ParseResult parseResult = new ParseResult(); 
		xPathParser.parse(xhtml.getBytes(), parseComponents, parseResult);
		
		log.info("Parse result: " + parseResult);
		
		assertNotNull( parseResult.getList("href") );
		assertTrue( parseResult.getList("href").size() > 0 );
		
	
	}
	
}
