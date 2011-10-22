package org.xylophones.webscraper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * A stateful class that holds information about 
 * a single instance of a robots.txt file
 */
public class RobotsTxt {
	
	private static final String ALLOW = "Allow:";
	
	private static final String DISALLOW = "Disallow:";
	
	private static final String USER_AGENT = "User-agent:";
	
	private static final String ALL_USER_AGENTS = "*";
	
	private final Set<String> allowed = new HashSet<String>();
	
	private final Set<String> disallowed = new HashSet<String>();

	public RobotsTxt(byte[] data, String userAgent) throws IOException {
		if ( data == null ) {
			return;
		}
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream( data );
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
		BufferedReader reader = new BufferedReader(inputStreamReader);
		
		String line = null;
		boolean inMatchingUserAgentSegment = false;
		boolean inUserAgentSegment = false;
		
		while ( ( line = reader.readLine() ) != null ) {
			
			// remove comments, if any
			if ( StringUtils.contains(line, '#') ) {
				line = StringUtils.substringBefore(line, "#");
			}
			
			// continue if whitespace or empty line
			line = line.trim();
			if ( line.length() == 0 ) {
				continue;
			}

			// check if entering a section applicable to our useragent
			if ( StringUtils.startsWith(line, USER_AGENT) ) {
				if ( ! inUserAgentSegment ) {
					inMatchingUserAgentSegment = false;
				}
				inUserAgentSegment = true;			
				
				String testUserAgent = (StringUtils.removeStart(line, USER_AGENT)).trim();
				if ( testUserAgent != null  ) {
					if ( userAgent.equals(testUserAgent) || ALL_USER_AGENTS.equals(testUserAgent) ) {
						inMatchingUserAgentSegment = true;
					}
				}
				
				continue;
			}

			// Add the relevant part to the allow or disallow sets 
			if ( line.startsWith(ALLOW) ) {
				inUserAgentSegment = false;
				if (inMatchingUserAgentSegment) {
					String allow = (StringUtils.removeStart(line, ALLOW)).trim();
					if ( allow != null && allow.length() > 0 ) {
						allowed.add(allow);
					}
				}
			}
			if ( line.startsWith(DISALLOW) ) {
				inUserAgentSegment = false;
				if (inMatchingUserAgentSegment) {
					String disallow = (StringUtils.removeStart(line, DISALLOW)).trim();
					if ( disallow != null && disallow.length() > 0 ) {
						disallowed.add(disallow);
					}
				}
			}
		}
	}
	
	/**
	 * Check if a path is allowed
	 * 
	 * @param pathAndQueryString
	 * @return
	 */
	public boolean isAllowed(String pathAndQueryString) {
		if ( pathMatches(allowed, pathAndQueryString) ) {
			return true;
		}
		if ( pathMatches(disallowed, pathAndQueryString) ) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check if a path matches the set of paths in a set according to 
	 * robots.txt rules
	 * 
	 * @param set
	 * @param path
	 * @return
	 */
	private boolean pathMatches(Set<String> set, String path) {
		for ( String setString : set ) {
			if ( setString.equals(path) ) {
				return true;
			}
			if ( setString.endsWith("/") ) {
				if ( path.startsWith(setString) ) {
					return true;
				}
			}
		}
		
		return false;
	}
	
}
