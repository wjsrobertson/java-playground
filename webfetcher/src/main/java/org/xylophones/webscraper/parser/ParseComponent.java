package org.xylophones.webscraper.parser;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Threadsafe since immutable & all fields are final
 */
public class ParseComponent {
	private final DataType dataType;
	private final String name;
	private final String definition;
	
	public DataType getDataType() {
		return dataType;
	}

	public String getName() {
		return name;
	}

	public String getDefinition() {
		return definition;
	}

	public ParseComponent(String name, String definition, DataType dataType) {
		this.dataType   = dataType;
		this.name       = name;
		this.definition = definition;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
