package org.xylophones.webscraper.config;

import java.util.Set;

import org.xylophones.webscraper.parser.ParseComponent;

public interface PageDefinitionLoader {

	public String[] getPageList();
	
	public Set<ParseComponent> getPageComponents(String name);
	
}
