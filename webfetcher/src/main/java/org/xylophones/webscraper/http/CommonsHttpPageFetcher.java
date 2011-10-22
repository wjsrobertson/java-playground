package org.xylophones.webscraper.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonsHttpPageFetcher implements PageFetcher {
	
	/**
	 * Logging instance
	 */
	private static final Log log = LogFactory.getLog(TidyDecoratingPageFetcher.class);
	
	private final String userAgent;
	
	public CommonsHttpPageFetcher(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * TODO - tidy up this code
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse fetchPage(String url) throws IOException {
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		
		client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		client.getParams().setParameter(HttpMethodParams.USER_AGENT, userAgent);

		ByteArrayInputStream in = null;
		byte[] responseBody = null;
		HttpResponse response = null;
		
		try {
		    // Execute the method.
		    int statusCode = client.executeMethod(method);
		    if (statusCode != HttpStatus.SC_OK) {
		        System.err.println("Method failed: " + method.getStatusLine());
		    }
		    
		    System.out.println("response code: " + statusCode);
			responseBody = method.getResponseBody();
			
			Header[] requestHeaders = method.getRequestHeaders();
			Map<String,String> headers = new HashMap<String,String>();
			for (Header header : requestHeaders) {
				if ( header.getName() != null && header.getValue() != null ) {
					headers.put( header.getName(), header.getValue() );
				} 
			}
			
			response = new HttpResponse(
				responseBody,
				statusCode,
				method.getStatusText(),
				headers
			);
			
		} catch (IOException e) {
			throw e;
		} finally {
			method.releaseConnection();
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// never mind
				}
			}
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Fetched HTML: \n" + new String(responseBody) );
		}
		
		return response;
	}
	
}
