package net.xylophones.jobfinder;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("pageRetriever")
public class PageRetriever {
	
	private static final Log log = LogFactory.getLog(PageRetriever.class);
	
	private String userAgent;
	
	@Autowired
	public PageRetriever(@Value("Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 5.2;") String userAgent) {
		this.userAgent = userAgent;
	}

	public String fetchUrl(String url) {
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		
		client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		client.getParams().setParameter(HttpMethodParams.USER_AGENT, userAgent);

		byte[] responseBody = null;

		try {
		    int statusCode = client.executeMethod(method);
		    if (statusCode != HttpStatus.SC_OK) {
		    	String errorMessage = "Method failed: " + method.getStatusLine();
				log.error(errorMessage);
		    	throw new RuntimeException("Failed to fetch page: " + errorMessage);
		    }
		    
			responseBody = method.getResponseBody();
			return new String(responseBody);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			method.releaseConnection();
		}
	}
}
