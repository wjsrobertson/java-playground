package net.xylophones.jobfinder;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
	
	private static final Logger log = Logger.getLogger(Application.class);
	
	public static void main(String args[]) {
		log.info("Starting application");
		AbstractApplicationContext appCtx = new ClassPathXmlApplicationContext("appCtx-jobFinder.xml");
		
		SearchProcessor searchProcessor = (SearchProcessor) appCtx.getBean("searchProcessor");
		searchProcessor.saveSearchResults("java");
	}	
}
