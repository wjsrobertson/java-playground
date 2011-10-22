package net.xylophones.megaproxy.plugins;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;

public interface ProxyPlugin {

	public HttpEntityEnclosingRequest processRequest(HttpEntityEnclosingRequest httpRequest);
	
	public HttpResponse processLocalResponse(HttpEntityEnclosingRequest httpRequest);
	
	public HttpResponse processResponse(HttpResponse httpResponse);
	
}
