package net.xylophones.midifinder.service.jpa;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.xylophones.midifinder.model.Url;
import net.xylophones.midifinder.model.UrlStatus;
import net.xylophones.midifinder.service.UrlService;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JpaUrlService implements UrlService {
	
	private EntityManager entityManager;
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(JpaUrlService.class);
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Url getById(int id) {
		return entityManager.find(Url.class, id);
	}
	
	@Override
	public void delete(Url url) {
		entityManager.remove(url);
		entityManager.flush();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Url> getAll() {
		Query query = entityManager.createNamedQuery("Url.findAll");
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Url> getAllUnretrieved() {
		Query query = entityManager.createNamedQuery("Url.findAllUnretrieved");
		return query.getResultList();
	}

	@Override
	public void save(Url url) {
		entityManager.persist(url);
		entityManager.flush();
	}

	@Override
	public void update(Url url) {
		entityManager.merge(url);
		entityManager.flush();
	}

	@Transactional 
	@SuppressWarnings("unchecked")
	private List<Url> getUnlockedByStatusAndMimeType(int maxNum, String mimeType, UrlStatus status) {
		//log.trace("Fetching unretrieved urls");
		Query query;
		if (mimeType == null) {
			query = entityManager.createNamedQuery("Url.findUnlockedByStatus");
			query.setParameter("status", status);
		} else {
			query = entityManager.createNamedQuery("Url.findUnlockedByStatusAndMimeType");
			query.setParameter("mimeType", mimeType);
			query.setParameter("status", status);
		}
		
		query.setFirstResult(0);
		query.setMaxResults(maxNum);
		List resultList = query.getResultList();
		//log.trace("retrieved " + resultList.size() + " urls");
		return resultList;
	}
	
	@Transactional //(propagation=Propagation.REQUIRES_NEW, isolation=Isolation.SERIALIZABLE, readOnly=true)
	@Override
	public List<Url> getAndLock(int maxNum, String mimeType, UrlStatus status) {
		List<Url> unretrieved = getUnlockedByStatusAndMimeType(maxNum, mimeType, status);
		
		for (Url url: unretrieved) {
			url.setLockedDate(new Date());
			entityManager.merge(url);
			entityManager.flush();
		}
		
		return unretrieved;
	}	
	
	public boolean exists(String host,String path, String queryString) {
		Query query = entityManager.createNamedQuery("Url.exists");
		query.setParameter("host", host);
		query.setParameter("path", path);
		query.setParameter("query", queryString);

		return (Long) query.getSingleResult() > 0;
	}
	
	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public List<Url> getAndLockPreferred(int maxNum, String mimeType, UrlStatus status) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * "); 
		sql.append(" FROM url  "); 
		sql.append(" WHERE locked_date IS NULL "); 
		sql.append("   AND status_code=200 "); 
		if (mimeType != null) {
			sql.append(" AND mime_type='"+mimeType+"' "); 
		}
		if (status != null) {
			sql.append("   AND status='"+status+"' "); 			
		}
		sql.append("   AND host in "); 	
		sql.append("   (  "); 	
		sql.append("       SELECT DISTINCT(host) FROM url where path like '%.mid' "); 	
		sql.append("   ) "); 	

		Query query = entityManager.createNativeQuery(sql.toString(), Url.class);
		query.setFirstResult(0);
		query.setMaxResults(maxNum);

		List<Url> resultList = query.getResultList();
		for (Url url: resultList) {
			url.setLockedDate(new Date());
			entityManager.merge(url);
			entityManager.flush();
		}

		return resultList;
	}

    @Transactional
    public void markRequestFailed(Url url) {
        url.setStatus(UrlStatus.REQUEST_FAILED);
        url.setLastRequestDate(new Date());
        url.setLockedDate(null);
        update(url);
    }

    @Transactional
    public void saveUrlsAndMarkUrlRetrieved(Set<Url> childUrls, Url url) {
        final int parentDepth = url.getDepth() == null ? 0: url.getDepth();
        final int childDepth = parentDepth+1;
        for (Url childUrl: childUrls) {

            childUrl.setDepth(childDepth);
            try {
                if (! exists(childUrl.getHost(), childUrl.getPath(), childUrl.getQuery()) ) {
                    save(childUrl);
                } else {
                    log.info("Not saving url since it is saved already: " + childUrl);
                }
            } catch (RuntimeException e) {
                log.warn("Couldn't save url: '" + childUrl + "' - " + e.getMessage());
            }
        }

        url.setStatus(UrlStatus.RETRIEVED);
        url.setLastRequestDate(new Date());
        url.setLockedDate(null);
        update(url);
    }

	public void releaseDbLock(Url url) {
		url.setLockedDate(null);
		update(url);
	}
}
