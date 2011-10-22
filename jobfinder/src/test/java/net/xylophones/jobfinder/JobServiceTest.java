package net.xylophones.jobfinder;

import java.util.Date;

import net.xylophones.jobfinder.model.Job;
import net.xylophones.jobfinder.service.JobService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:appCtx-jobFinder.xml"})
public class JobServiceTest {
	
	@Autowired
	private JobService jobService;

	@Test
	public void testX() {
		Job job = new Job();
		job.setDate(new Date());
		job.setMaxRate(100);
		
		jobService.save(job);
	}
	
}
