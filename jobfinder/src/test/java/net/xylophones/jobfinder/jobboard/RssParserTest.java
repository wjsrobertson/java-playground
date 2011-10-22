package net.xylophones.jobfinder.jobboard;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.core.io.ClassPathResource;

@RunWith(JUnit4.class)
public class RssParserTest {

	private File resultsFile;
	
	private String resultsFileContents;
	
	RssParser parser;

	@Before
	public void setUp() throws IOException {
		ClassPathResource cpr = new ClassPathResource("results.xml");
		resultsFile = cpr.getFile();
		resultsFileContents = readFileToString(resultsFile);
		
		parser = new RssParser();
	}
	
	@Test
	public void testX() {
		List<RssItem> jobs = parser.parseJobsFromRss(resultsFileContents);
		assertEquals(10,jobs.size());
		System.out.println(jobs);
	}

}
