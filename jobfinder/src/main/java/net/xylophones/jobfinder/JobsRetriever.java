package net.xylophones.jobfinder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.xylophones.jobfinder.jobboard.RssItem;
import net.xylophones.jobfinder.jobboard.RssParser;
import net.xylophones.jobfinder.jobboard.RssSearchUrlGenerator;
import net.xylophones.jobfinder.jobboard.RssToJob;
import net.xylophones.jobfinder.model.Job;

@Component("jobsRetriever")
public class JobsRetriever {
	
	private static final Log log = LogFactory.getLog(JobsRetriever.class);
	
	private RssSearchUrlGenerator rssSearchUrlGenerator;
	
	private PageRetriever pageRetriever;
	
	private RssParser rssParser;
	
	private RssToJob rssToJob;

	@Autowired
	public JobsRetriever(RssSearchUrlGenerator rssSearchUrlGenerator, PageRetriever pageRetriever, RssParser rssParser, RssToJob rssToJob) {
		this.rssSearchUrlGenerator = rssSearchUrlGenerator;
		this.pageRetriever = pageRetriever;
		this.rssParser = rssParser;
		this.rssToJob = rssToJob;
	}
	
	public List<Job> fetchJobs(String searchTerm) {
		int currentPage = 1;
		int maxPages = 100;

		List<Job> jobs = new ArrayList<Job>();
		List<RssItem> previousRssItems = new ArrayList<RssItem>();

		while( currentPage < maxPages ) {
			String url = rssSearchUrlGenerator.generateUrl(currentPage, searchTerm);
			log.info("Retrieving: " + url);
			String page = pageRetriever.fetchUrl(url);
			
			List<RssItem> rssItems = rssParser.parseJobsFromRss(page);
			int numResultsFound = rssItems.size();
			
			if (numResultsFound == 0) {
				log.info("Hit empty results page");
				break;
			} else {
				log.info("Found " + rssItems.size() + " results");
			}
			
			if (rssItems.equals(previousRssItems)) {
				log.info("Found duplicate page");
				break;
			}
			
			for (RssItem rssItem: rssItems) {
				try {
					Job job = rssToJob.toJob(rssItem);
					job.setSearchTerm(searchTerm);
					jobs.add( job );
				} catch (RuntimeException e) {
					log.warn("Couldn't parse job: " + rssItem);
				}
			}
			
			// we have reached the last page
			if (numResultsFound < 10) {
				break;
			}
			
			previousRssItems = rssItems;
			currentPage++;
			pause();
		}
		
		log.info("Found " + jobs.size() + " jobs for term '" + searchTerm + "'");
		
		return jobs;
	}

	private void pause() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
}
