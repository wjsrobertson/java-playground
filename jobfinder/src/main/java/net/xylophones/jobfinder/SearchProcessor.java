package net.xylophones.jobfinder;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.xylophones.jobfinder.model.Job;
import net.xylophones.jobfinder.service.JobService;

@Component("searchProcessor")
public class SearchProcessor {
	
	private static final Log log = LogFactory.getLog(SearchProcessor.class);

	private JobsRetriever jobsRetriever;

	private JobService jobService;

	@Autowired
	public SearchProcessor(JobsRetriever jobsRetriever, JobService jobService) {
		this.jobsRetriever = jobsRetriever;
		this.jobService = jobService;
	}

	public void saveSearchResults(String searchTerm) {
		List<Job> jobs = jobsRetriever.fetchJobs(searchTerm);
		for (Job job: jobs) {
			try {
				jobService.save(job);
			} catch (RuntimeException e) {
				log.error("Problem saving job: " + job + ": " + e);
			}
		}
	}
}
