package net.xylophones.midifinder.service;

import java.util.List;

import net.xylophones.midifinder.model.Url;
import net.xylophones.midifinder.model.UrlStatus;

public interface UrlService {
	
	public Url getById(int id);
	
	public void save(Url url);
	
	public void update(Url url);
	
	public void delete(Url url);
	
	public List<Url> getAll();
	
	public List<Url> getAllUnretrieved();
	
	//public List<Url> getUnlockedByStatus(int maxNum, String mimeType, UrlStatus status);
	
	public List<Url> getAndLock(int maxNum, String mimeType, UrlStatus staus);
	
	public boolean exists(String host,String path,String query);
	
	public List<Url> getAndLockPreferred(int maxNum, String mimeType, UrlStatus status);

}
