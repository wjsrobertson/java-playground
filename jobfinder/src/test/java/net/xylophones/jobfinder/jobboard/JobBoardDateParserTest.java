package net.xylophones.jobfinder.jobboard;

import java.io.IOException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JobBoardDateParserTest {
	
	JobBoardDateParser jobBoardDateParser;

	@Before
	public void setUp() throws IOException {
		jobBoardDateParser = new JobBoardDateParser();
	}
	
	@Test
	public void testX() {
		Date date = jobBoardDateParser.parse("Wed, 06 Oct 2010 14:41:00 +0100");
		System.out.println(date);
		
		assertEquals( 9, date.getMonth() );
		assertEquals( 110, date.getYear() );
	}
	
}
