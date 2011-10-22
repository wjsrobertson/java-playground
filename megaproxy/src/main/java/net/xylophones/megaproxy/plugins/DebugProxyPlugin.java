package net.xylophones.megaproxy.plugins;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;

public class DebugProxyPlugin extends AbstractProxyPlugin implements ProxyPlugin {
	
	private final Log log = LogFactory.getLog(DebugProxyPlugin.class);

	@Override
	public HttpEntityEnclosingRequest processRequest(HttpEntityEnclosingRequest request) {
		String uuid = (String) request.getParams().getParameter("uuid");
		log.debug("Request\t"+uuid+"\t"+request.getRequestLine());
		return request;
	}

	@Override
	public HttpResponse processResponse(HttpResponse response) {
		String uuid = (String) response.getParams().getParameter("uuid");
		log.debug("Response\t"+uuid+"\t"+response.getStatusLine());
		return response;
	}

}
