package net.xylophones.jobfinder.jobboard;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import net.xylophones.jobfinder.model.Job;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DescriptionParserTest {
	
	private DescriptionParser underTest;
	
	@Before
	public void setUp() {
		underTest = new DescriptionParser();
	}

	@Test
	public void testParsed() {
		Job job = new Job();
		underTest.parseDescription(getSampleDescription("Negotiable"), job);

		assertEquals("Some description..", job.getDescription());
		assertEquals("Huxley Associates - London", job.getAdvertiser());
		assertEquals("negotiable", job.getRateDescription());
	}
	
	@Test
	public void testRange() {
		String[] salaries = {
			"£500 - £700 per day",
			"£500 - £700 a day",
		 	"£500 - £700 per day x 1 depending on experience",
			"£500-700 A DAY",
			"£500-700 per day",
			"£500-£700 A DAY",
		 	"£500/700/Day",
			"£500 to £700 per day",
			"500-700",
			"£0.0000 per month + 500-700",
			"£350 - £370 per annum + £500 - £700 / Day",
			"£0.0000 per month + £500-700",
			"&pound;500 to &pound;700 per day",
		};
		
		Job job;
		for (String salary: salaries) {
			job = new Job();
			
			underTest.parseDescription(getSampleDescription(salary), job);
			
			assertEquals(500, (int) job.getMinRate());
			assertEquals(700, (int) job.getMaxRate());
		}
	}
	
	@Test
	public void testMaxRate() {
		String[] salaries = {
			"Up to £700 a day",
			"to £700.00 per day - depending on experience",
		 	"Up to UKP 700 per day",
			"£550 - £600 per month + To £700 per day",
			"up to 700 per day"
		};
		
		Job job;
		for (String salary: salaries) {
			job = new Job();
			
			underTest.parseDescription(getSampleDescription(salary), job);

			assertEquals(700, (int) job.getMaxRate());
		}
	}
	
	@Test
	public void testSingleRate() {
		String[] salaries = {
			"£500.00 per day.",
			"£500 A Day",
		 	"£500.00 per day",
		 	"500 Per Day",
		 	"£500.00 per day.",
		 	"£ 500 per day",
		 	"£500 PD",
		 	"rate - £500 per day",
		 	"£500/per day",
		 	"£500 - per day"
		};
		
		Job job;
		for (String salary: salaries) {
			job = new Job();
			
			underTest.parseDescription(getSampleDescription(salary), job);
			
			assertEquals(500, (int) job.getMinRate());
			assertEquals(500, (int) job.getMaxRate());
		}
	}
	
	@Test
	public void testMinRate() {
		String[] salaries = {
			"£500+ per day",
		};
		
		Job job;
		for (String salary: salaries) {
			job = new Job();
			
			underTest.parseDescription(getSampleDescription(salary), job);
			
			assertEquals(500, (int) job.getMinRate());
			assertNull(job.getMaxRate());
		}
	}
	
	
	@Test
	public void testMarketRate() {
		String[] salaries = {
			"Market Rate per day",
			"Market Rate",
		};
		
		Job job;
		for (String salary: salaries) {
			job = new Job();
			
			underTest.parseDescription(getSampleDescription(salary), job);
			
			assertEquals("market rates", job.getRateDescription());
			assertNull(job.getMaxRate());
		}
	}
	
	@Test
	public void testNegotiable() {
		String[] salaries = {
			"£500 - £700 per day + negotiable to attract the best",
			"neg"
		};
		
		Job job;
		for (String salary: salaries) {
			job = new Job();
			
			underTest.parseDescription(getSampleDescription(salary), job);
			
			assertEquals("negotiable", job.getRateDescription());
			assertNull(job.getMaxRate());
		}
	}	
	
	@Test
	public void testUnspecified() {
		String[] salaries = {
				"",
				" ",
		};
		
		Job job;
		for (String salary: salaries) {
			job = new Job();
			
			underTest.parseDescription(getSampleDescription(salary), job);
			
			assertEquals("unspecified", job.getRateDescription());
			assertNull(job.getMaxRate());
		}
	}	

	private String getSampleDescription(String salary) {
		String sample = "Job Description : Some description..<br/>\n" + 
		"Advertiser : Huxley Associates - London<br/>\n" + 
		"Location : London - City<br/>\n" +
		"Salary : " + salary;
		
		return sample;
	}
	
}
