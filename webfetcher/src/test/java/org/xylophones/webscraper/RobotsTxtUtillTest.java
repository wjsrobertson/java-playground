package org.xylophones.webscraper;

import junit.framework.TestCase;

public class RobotsTxtUtillTest extends TestCase {

	public void testAllowDisallowSimple() throws Exception {
		
		StringBuilder builder = new StringBuilder();
		
		String[] agents = {
			"*",
			"banana agent"
		};
		
		for ( String agent : agents ) {
			builder.append("User-agent: *\r\n");
			builder.append("Allow: /allowdir/\r\n");
			builder.append("Allow: /allowfile\r\n");
			builder.append("Allow: /anotherallowfile # with comment\r\n");
			builder.append("Disallow: /disallowdir/\r\n");
			builder.append("Disallow: /disallowfile\r\n");
			builder.append("User-agent: different agent\r\n");
			builder.append("Disallow: /fakedisallowfile\r\n");
			
			String robotsTxt = builder.toString();
			
			RobotsTxt robotsTxtUtil = new RobotsTxt(robotsTxt.getBytes(), agent);
			
			assertTrue( robotsTxtUtil.isAllowed("/allowfile") );
			assertTrue( robotsTxtUtil.isAllowed("/allowdir/anyfile") );
			assertTrue( robotsTxtUtil.isAllowed("/anotherallowfile") );
			assertTrue( robotsTxtUtil.isAllowed("/allowdir") );
			assertTrue( robotsTxtUtil.isAllowed("/allowdir/") );
			assertTrue( robotsTxtUtil.isAllowed("/fakedisallowfile") );
			assertTrue( robotsTxtUtil.isAllowed("/missingfile") );	
			
			assertFalse( robotsTxtUtil.isAllowed("/disallowfile") );
			assertFalse( robotsTxtUtil.isAllowed("/disallowdir/anyfile") );
			assertFalse( robotsTxtUtil.isAllowed("/disallowdir/") );
		}	
	}
}
