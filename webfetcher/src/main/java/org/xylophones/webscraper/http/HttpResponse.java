package org.xylophones.webscraper.http;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * Immutable object holding information about an HTTP response
 */
public class HttpResponse {

	private final byte[] data;
	
	private final int statusCode;
	
	private final String statusString;
	
	private final Map<String,String> simpleHeaders;
	
	public HttpResponse(byte[] data, int statusCode, String statusString, Map<String,String> simpleHeaders) {
		this.data = data;
		this.statusCode = statusCode;
		this.statusString = statusString;
		this.simpleHeaders = Collections.unmodifiableMap(simpleHeaders);
	}

	public byte[] getData() {
		return Arrays.copyOf(data, data.length);
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusString() {
		return statusString;
	}

	public Map<String, String> getSimpleHeaders() {
		return simpleHeaders;
	}
	
}
