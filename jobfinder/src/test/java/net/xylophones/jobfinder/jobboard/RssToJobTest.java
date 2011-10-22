package net.xylophones.jobfinder.jobboard;

import static org.junit.Assert.assertEquals;
import net.xylophones.jobfinder.model.Job;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class RssToJobTest {
	
	@Mock
	private DescriptionParser descriptionParserMock;
	
	@Mock
	private JobBoardDateParser jobBoardDateParser;
	
	private RssToJob underTest;
	
	@Before
	public void setUp() {
		underTest = new RssToJob(descriptionParserMock,jobBoardDateParser);
	}
	
	@Test
	public void testX() {
		RssItem item = new RssItem();
		item.setLink("http://www.theitjobboard.co.uk/IT-Job/Software-Engineering-Lead-Software-Engineer-Software-Manager-Java-Flash-ActionScript-HTML5-JavaScript-OpenTV-MHEG5-Perl-London-South-East/8001969/en/?source=rss&amp;xc=247&amp;WT.mc_id=R104&amp;SearchTerms=java&amp;LocationSearchTerms=london&amp;JobTypeFilter=1&amp;DatePostedFilter=0&amp;Page=1&amp;OrderBy=1&amp;CountryId=0</link>c");
		
		Job job = underTest.toJob(item);
		assertEquals(8001969, (int) job.getExternalId());
	}
	
}
