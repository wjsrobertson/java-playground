package net.xylophones.jobfinder.service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.xylophones.jobfinder.model.Job;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component("urlService")
@Resource
public class JobService {

	private EntityManager entityManager;

	private static final Logger log = Logger.getLogger( JobService.class );

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional 
	public void save(Job job) {
		entityManager.merge(job);
	}

}
