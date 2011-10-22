package org.xylophones.webscraper.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Threadsafe - multiple threads may put and get values at the same time since
 * the underlying data structures have sufficient synchronisation.
 */
public class ParseResult {
	private final Map<String,String> strings = Collections.synchronizedMap( new HashMap<String,String>() );
	private final Map<String,List<String>> lists = Collections.synchronizedMap( new HashMap<String,List<String>>() );
	
	public void putString(String name, String value) {
		strings.put(name, value);
	}
	public String getString(String name) {
		return strings.get(name);
	}
	
	public List<String> getList(String name) {
		return lists.get(name);
	}
	public void putList(String name, List<String> value) {
		lists.put(name, value);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
