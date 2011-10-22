package org.xylophones.webscraper.config;

import junit.framework.TestCase;

public class FilePageDefinitionLoaderTest extends TestCase {

	public void testLoadXml() {
		
		FilePageDefinitionLoader loader = new FilePageDefinitionLoader();
		loader.init("/home/poobar/eclipse-workspace/WebFetcher/src/test/resources/test.xml");
		//loader.init("classpath:test.xml");
		
		assertTrue( loader.getPageComponents("yahoo").size() > 0 );
	}
	
}
