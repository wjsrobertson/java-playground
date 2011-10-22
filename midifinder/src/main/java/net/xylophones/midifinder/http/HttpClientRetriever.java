package net.xylophones.midifinder.http;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component("retriever")
public class HttpClientRetriever implements Retriever {
	
	/**
	 * Logging instance
	 */
	private static final Log log = LogFactory.getLog(HttpClientRetriever.class);

	private final HttpClient client;
	
	private final int totalConnections = 20;
	
	private final int timeout = 2000;
	
	private final int socketTimeout = 10000;
	
	public HttpClientRetriever() {
		this("Test");
	}
	
	public HttpClientRetriever(String userAgent) {
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		connectionManager.getParams().setDefaultMaxConnectionsPerHost(1);
		connectionManager.getParams().setMaxTotalConnections(totalConnections);
		connectionManager.getParams().setConnectionTimeout(timeout);
		connectionManager.getParams().setStaleCheckingEnabled(true);
		connectionManager.getParams().setSoTimeout(socketTimeout);

      	client = new HttpClient(connectionManager);
		client.getParams().setParameter(HttpMethodParams.USER_AGENT, userAgent);
		//client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
	}
	
	@Override
	public HeadMethod getHead(String url) throws IOException {
		log.debug("begin HEAD request for " + url);
		
		HeadMethod method = new HeadMethod(url);

		try {
		    int statusCode = client.executeMethod(method);
		    if (statusCode != HttpStatus.SC_OK) {
		    	log.warn("Method failed: " + method.getStatusLine() + "(" + url + ")");
		    }
		} catch (IOException e) {
			log.debug("HEAD failed for " + url + " - " + e.getMessage());
			throw e;
		} catch (Exception e) {
			log.debug("HEAD failed for " + url + " - " + e.getMessage());
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			method.releaseConnection();
		}
		
		log.debug("end HEAD request for " + url);

		return method;
	}
	
	@Override
	public byte[] getBody(String url) throws IOException {
		log.debug("begin GET request for " + url);
		
		HttpMethod method = new GetMethod(url);
		
		try {
		    int statusCode = client.executeMethod(method);
		    if (statusCode != HttpStatus.SC_OK) {
		        log.warn("Method failed: " + method.getStatusLine());
		    }

			log.debug("end GET request for " + url);
			return method.getResponseBody();
		} catch (IOException e) {
			log.debug("GET failed for " + url + " - " + e.getMessage());
			throw e;
		} catch (Exception e) {
			log.debug("GET failed for " + url + " - " + e.getMessage());
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			method.releaseConnection();
		}
	}
}
