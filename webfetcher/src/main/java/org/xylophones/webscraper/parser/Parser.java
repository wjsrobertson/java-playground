package org.xylophones.webscraper.parser;


public interface Parser {

	public void parse(byte[] data, ParseComponent[] parseComponents, ParseResult parseResult) throws ParseException;
	
}
