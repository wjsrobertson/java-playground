package net.xylophones.megaproxy.plugins;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;

public class AbstractProxyPlugin implements ProxyPlugin {

	@Override
	public HttpEntityEnclosingRequest processRequest(HttpEntityEnclosingRequest request) {
		return request;
	}
	
	@Override
	public HttpResponse processLocalResponse(HttpEntityEnclosingRequest httpRequest) {
		return null;
	}

	@Override
	public HttpResponse processResponse(HttpResponse response) {
		return response;
	}

}
