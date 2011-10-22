package net.xylophones.midifiles.service.jpa;

import java.util.List;

import net.xylophones.midifinder.model.Url;
import net.xylophones.midifinder.model.UrlStatus;
import net.xylophones.midifinder.service.UrlService;
import net.xylophones.midifinder.service.jpa.JpaUrlService;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.AbstractSpringContextTests;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit38.AbstractTransactionalJUnit38SpringContextTests;

//@ContextConfiguration("classpath:appCtx-midiFnder.xml")
//@Runwith(JUnit38ClassRunner.class)
public class JpaUrlServiceTest extends AbstractTransactionalSpringContextTests {

	private UrlService urlService;
	
	private Logger log = Logger.getLogger(JpaUrlServiceTest.class);

	protected String[] getConfigLocations() {
		return new String[]{"classpath:appCtx-midiFnder.xml"};
	}
	
	@Before
	public void onSetUp() {
		BasicConfigurator.configure();
		urlService = (JpaUrlService) applicationContext.getBean("urlService", JpaUrlService.class);
	}
	
	/*
	//@Rollback(true)
	@Test
	public void testX() {
		Url url = new Url();
		url.setDataSize(101);
		url.setHost("www.example.com");
		url.setMimeType("test/test");
		url.setPath("/banana");
		url.setStatus(UrlStatus.NEW);

		urlService.save(url);
		
		log.debug("id: " + url.getId());
	}
		
	//@Rollback(true)
	@Test
	public void testFinnUnretrieved() {
		Url url = new Url();
		url.setDataSize(101);
		url.setHost("www.example.com");
		url.setMimeType("test/test");
		url.setPath("/banana2");
		url.setStatus(UrlStatus.NEW);

		urlService.save(url);
		
		Url url2 = new Url();
		url2.setDataSize(101);
		url2.setHost("www.example2.com");
		url2.setMimeType("test/test");
		url2.setPath("/banana");
		url2.setStatus(UrlStatus.HEADERS_RETRIEVED);
		
		urlService.save(url2);
		
		List<Url> unretrieved = urlService.getAndLock(1, "test/test", UrlStatus.NEW);
		assertEquals(1, unretrieved.size());
	}
	*/
	
	public void testY() {
		List<Url> unretrieved = urlService.getAndLockPreferred(1, "text/html", UrlStatus.HEADERS_RETRIEVED);
		assertTrue(unretrieved.size() > 0);
	}
	
}
