package net.xylophones.megaproxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import net.xylophones.megaproxy.io.Connection;
import net.xylophones.megaproxy.io.ConnectionManager;
import net.xylophones.megaproxy.io.ConnectionManagerSource;
import net.xylophones.megaproxy.io.HeaderUtils;
import net.xylophones.megaproxy.io.HttpInputStream;
import net.xylophones.megaproxy.io.HttpOutputStream;
import net.xylophones.megaproxy.io.IOUtil;
import net.xylophones.megaproxy.plugins.ProxyPlugin;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;

public class RequestProcessor implements Callable<Boolean> {
	
	private static final Logger log = Logger.getLogger(RequestProcessor.class);
	
	private volatile Socket downstreamSocket;
	
	private Connection upstreamConnection;

	private final ConnectionManager connectionManager = ConnectionManagerSource.getConnectionManager();
	
	private volatile List<ProxyPlugin> proxyPlugins = new ArrayList<ProxyPlugin>();

	public Socket getDownstreamSocket() {
		return downstreamSocket;
	}
	
	public void setDownstreamSocket(Socket downstreamSocket) {
		this.downstreamSocket = downstreamSocket;
	}

	public List<ProxyPlugin> getProxyPlugins() {
		return proxyPlugins;
	}

	public void setProxyPlugins(List<ProxyPlugin> proxyPlugins) {
		this.proxyPlugins = proxyPlugins;
	}

	@Override
	public Boolean call() throws Exception {
		try {
			processConnection();
			return true;
		} catch (IOException ieo) {
			return false;
		} finally {
			if ( downstreamSocket != null && downstreamSocket.isConnected() && ! downstreamSocket.isClosed()) {
				downstreamSocket.close();
			}
			if ( upstreamConnection != null ) {
				upstreamConnection.close();
			}
		}
	}
	
	/**
	 * Read an HTTP request from a client, send it to a server and return the
	 * server's response to the client
	 * 
	 * @return
	 * @throws IOException
	 */
	private void processConnection() throws IOException {
		HttpInputStream downInputStream = new HttpInputStream(
			new BufferedInputStream( downstreamSocket.getInputStream() )
		);
		
		HttpOutputStream downOutputStream = new HttpOutputStream(
				new BufferedOutputStream( downstreamSocket.getOutputStream() )
		);
		
		HttpOutputStream upOutputStream = null;
		HttpInputStream upInputStream   = null;		
		
		boolean keepAliveDownStream = true;
		while (keepAliveDownStream) {
			final HttpParams httpParams = createHttpParams();

			HttpEntityEnclosingRequest httpRequest = (HttpEntityEnclosingRequest) downInputStream.readHttpMessage();
			httpRequest.setParams(httpParams);

			cleanRequestForUpstream(httpRequest);
			URL url = new URL(httpRequest.getRequestLine().getUri());
			log.debug("Retrieved request for: " + url);
			
			keepAliveDownStream = HeaderUtils.isProxyConnectionKeepAlive(httpRequest);
			
			// fetch the request body
			
			HttpEntity requestEntity = fetchEntity(downInputStream, httpRequest);
			if (requestEntity != null) {
				httpRequest.setEntity(requestEntity);
			}

			HttpResponse httpResponse = processLocalResponse(httpRequest);
			
			if (httpResponse == null) {
				httpRequest = processRequest(httpRequest);

				// Connect upstream if required
				upstreamConnection = getUpstreamConnection(url);
				upOutputStream = upstreamConnection.getOutputStream();
				upInputStream  = upstreamConnection.getInputStream();

				// write request upstream
				if (httpRequest.getEntity() != null) {
					HeaderUtils.setContentLength(httpRequest, requestEntity.getContentLength());
					HeaderUtils.stripTransferEncoding(httpRequest);
				}
				upOutputStream.writeHttpMessage(httpRequest);

				if (httpRequest.getEntity() != null) {
					log.debug("Copying response data downstream");
					IOUtils.copy(httpRequest.getEntity().getContent(), upOutputStream);
				}
				upOutputStream.flush();
				log.debug("Sent request");
		
				// read the headers and body from upstream response
				httpResponse = (HttpResponse) upInputStream.readHttpMessage();
				httpResponse.setParams(httpParams);
				cleanResponseForDownStream(httpResponse);

				HttpEntity responseEntity = fetchEntity(upInputStream, httpResponse);
				if (responseEntity != null) {
					httpResponse.setEntity(responseEntity);
				}
				
				// close upstream connection if required
				if ( HeaderUtils.isConnectionClose(httpResponse) ) {
					log.debug("Closing upstream connection");
					upstreamConnection.close();
				}
			}

			httpResponse = processResponse(httpResponse);

			// write the response downstream
			if (httpResponse.getEntity() != null) {
				HeaderUtils.setContentLength(httpResponse, httpResponse.getEntity().getContentLength());
				HeaderUtils.stripTransferEncoding(httpResponse);
			}
			downOutputStream.writeHttpMessage(httpResponse);

			if (httpResponse.getEntity() != null) {
				log.debug("Copying response data downstream");
				IOUtils.copy(httpResponse.getEntity().getContent(), downOutputStream);
			}
			
			downOutputStream.flush();

			log.debug("Sent response");
			log.debug("Request/response complete");
			
			if ( HeaderUtils.isConnectionClose(httpRequest) || HeaderUtils.isProxyConnectionClose(httpRequest) ) {
				log.debug("Closing downstream connection");
				downstreamSocket.close();
			}
		}
	}
	
	private HttpEntity fetchEntity(HttpInputStream inputStream, HttpMessage httpMessage) throws IOException {
		if (HeaderUtils.hasBody(httpMessage)) {
			log.debug("Body expected");
			ByteArrayOutputStream byteOutSream = new ByteArrayOutputStream();
			
			if ( HeaderUtils.isTransferEncodingChunked(httpMessage) ) {
				log.debug("Copying chunked body");
				IOUtil.copyChunkedToNonChunked(inputStream, byteOutSream);
			} else {
				int contentLength = HeaderUtils.getContentLength(httpMessage);
				
				if ( contentLength >= 0 ) {
					log.debug("Copying " + contentLength + " bytes");
					IOUtil.copy(inputStream, byteOutSream, contentLength);
				} else {
					log.debug("Copying full body");
					IOUtils.copy(inputStream, byteOutSream);
				}					
			}
			
			byte[] responseData = byteOutSream.toByteArray();

			ByteArrayEntity responseEntity = new ByteArrayEntity(
				responseData
			);
			
			return responseEntity;
		} else {
			log.debug("Body not expected");
			return null;
		}
	}
	
	private Connection getUpstreamConnection(URL url) throws IOException  {
		return connectionManager.getConnection(url);
	}

	/**
	 * Alter headers of a client's http request to make them suitable for
	 * sending to a remote server
	 * 
	 * @param httpRequest
	 */
	private void cleanRequestForUpstream(HttpRequest httpRequest) {
		httpRequest.removeHeader( httpRequest.getFirstHeader("Proxy-Connection") );
	}
	
	/**
	 * Alter headers of an HTTP response to make them suitable for returning
	 * to downstream client
	 * 
	 * @param httpResponse
	 */
	private void cleanResponseForDownStream(HttpResponse httpResponse) {
	}
	
	private HttpEntityEnclosingRequest processRequest(HttpEntityEnclosingRequest request) {
		log.debug("Processing request with plugins");
		for (ProxyPlugin proxyPlugin : proxyPlugins) {
			request = proxyPlugin.processRequest(request);
		}
		
		return request;
	}
	
	private HttpResponse processResponse(HttpResponse response) {
		log.debug("Processing response with plugins");
		for (ProxyPlugin proxyPlugin : proxyPlugins) {
			response = proxyPlugin.processResponse(response);
		}
		return response;
	}
	
	public HttpResponse processLocalResponse(HttpEntityEnclosingRequest httpRequest) {
		for (ProxyPlugin proxyPlugin : proxyPlugins) {
			HttpResponse response = proxyPlugin.processLocalResponse(httpRequest);
			if (response != null) {
				return response;
			}
		}
		return null;
	}
	
	private HttpParams createHttpParams() {
		final UUID uuid = UUID.randomUUID();	// unique UUID for request
		final BasicHttpParams httpParams = new BasicHttpParams();
		httpParams.setParameter( "uuid", uuid.toString().replace("-", "") );
		
		return httpParams;
	}
}