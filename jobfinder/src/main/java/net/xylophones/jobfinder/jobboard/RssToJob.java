package net.xylophones.jobfinder.jobboard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.xylophones.jobfinder.model.Job;

@Component("rssToJob")
public class RssToJob {

	private DescriptionParser descriptionParser;
	
	private JobBoardDateParser jobBoardDateParser;

	@Autowired
	public RssToJob(DescriptionParser descriptionParser, JobBoardDateParser jobBoardDateParser) {
		this.descriptionParser = descriptionParser;
		this.jobBoardDateParser  = jobBoardDateParser;
	}

	public Job toJob(RssItem rss) {
		Job job = new Job();
		descriptionParser.parseDescription(rss.getDescription(), job);
		job.setTitle( rss.getTitle() );
		job.setExternalId( parseExternalId( rss.getLink()) );
		job.setTitle(rss.getTitle());
		job.setDate( jobBoardDateParser.parse(rss.getPublicationDate()) );
		
		return job;
	}
	
	private Integer parseExternalId(String link) {
		Pattern myPattern = Pattern.compile("/(\\d+)/");
		Matcher matcher = myPattern.matcher(link);

		if (matcher.find()) {
			return Integer.valueOf(matcher.group(1));
		}
		
		return null;
	}
}
