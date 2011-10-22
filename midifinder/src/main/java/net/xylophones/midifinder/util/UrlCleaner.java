package net.xylophones.midifinder.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UrlCleaner {

	private static final Log log = LogFactory.getLog(UrlCleaner.class);
	
	private final int maxLoop = 20;
	
	public String cleanPath(String path) {
		if (path == null) {
			return path;
		}
		
		log.debug("Cleaning: " + path);
		
		int loop = 0;
		while (path.matches("^/\\.\\./.*")) {
			path = path.replace("/../", "/");
			if (loop++ > maxLoop) {
				throw new RuntimeException("Loop count exceeded for: " + path);
			}
		}
		
		loop = 0;
		while (path.contains("/../")) {
			path = path.replaceAll("/[^\\/]+?/../", "/");
			if (loop++ > maxLoop) {
				throw new RuntimeException("Loop count exceeded for: " + path);
			}
		}
		
		loop = 0;
		while (path.contains("/./")) {
			path = path.replaceAll("/./", "/");
			if (loop++ > maxLoop) {
				throw new RuntimeException("Loop count exceeded for: " + path);
			}
		}
		log.debug("Cleaned: " + path);

		return path;
	}	
}

