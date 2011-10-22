package net.xylophones.megaproxy.plugins;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.FileEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;


public class LocalFileProxyPlugin extends AbstractProxyPlugin {

	private final Log log = LogFactory.getLog(LocalFileProxyPlugin.class);
	
	private Map<String,String> targets = new HashMap<String,String>();
	
	{
		String url = "http://www.kitco.com/";
		String dst = "/tmp/test.html";
		targets.put(url, dst);
	}
	
	@Override
	public HttpResponse processLocalResponse(HttpEntityEnclosingRequest httpRequest) {
		String uri = httpRequest.getRequestLine().getUri();
		
		for (Map.Entry<String,String> entry: targets.entrySet()) {
			String targetUri = entry.getKey();
			String fileName  = entry.getValue();
			
			if (targetUri.equals(uri)) {
				File file = new File(fileName);
				String mimeType  = null;
				
				try {
					MagicMatch magicMatch = Magic.getMagicMatch(file, true, true);
					mimeType = magicMatch.getMimeType();
				} catch (MagicParseException e) {
					log.error(e);
				} catch (MagicMatchNotFoundException e) {
					log.error(e);
				} catch (MagicException e) {
					log.error(e);
				}
				
				FileEntity entity = new FileEntity(file, mimeType);
				
				HttpResponse httpResponse = getHttpResponse();
				httpResponse.setEntity(entity);
				
				return httpResponse;
			}
		}
		
		log.debug("uri: " + uri);
		
		return null;
	}
	
	private HttpResponse getHttpResponse() {
		HttpVersion httpVersion = new HttpVersion(1, 1);
		StatusLine statusLine = new BasicStatusLine(httpVersion, 200, "OK");
		
		return new BasicHttpResponse(statusLine);
	}
}
