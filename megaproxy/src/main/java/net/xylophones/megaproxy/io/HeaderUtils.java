package net.xylophones.megaproxy.io;

import org.apache.http.Header;
import org.apache.http.HttpMessage;
import org.apache.http.message.BasicHeader;

public class HeaderUtils {

	public static int getContentLength(HttpMessage httpMessage) {
		Header contentLengthHeader = httpMessage.getFirstHeader("Content-Length");
		if (contentLengthHeader != null) {
			String contentLengthString = contentLengthHeader.getValue().trim();
			Integer contentLength = Integer.valueOf(contentLengthString);

			return contentLength;
		}
		
		return -1;
	}
	
	public static void setContentLength(HttpMessage httpMessage, long length) {
		httpMessage.removeHeader( httpMessage.getFirstHeader("Content-Length") );
		httpMessage.addHeader(new BasicHeader("Content-Length", ""+length));
	}
	
	public static void stripTransferEncoding(HttpMessage httpMessage) {
		Header transferEncoding = httpMessage.getFirstHeader("Transfer-Encoding");
		if (transferEncoding != null) {
			httpMessage.removeHeader(transferEncoding);
		}
	}
	
	public static boolean isTransferEncodingChunked(HttpMessage httpMessage) {
		Header keepAliveHeader = httpMessage.getFirstHeader("Transfer-Encoding");
		
		if ( keepAliveHeader!=null ) {
			return "chunked".equals( keepAliveHeader.getValue().trim() );
		}
		
		return false;
	}
	
	public static boolean isProxyConnectionKeepAlive(HttpMessage httpMessage) {
		Header keepAliveHeader = httpMessage.getFirstHeader("Proxy-Connection");
		
		if ( keepAliveHeader!=null ) {
			return "keep-alive".equals( keepAliveHeader.getValue().trim() );
		}
		
		return false;
	}
	
	public static boolean isProxyConnectionClose(HttpMessage httpMessage) {
		Header keepAliveHeader = httpMessage.getFirstHeader("Proxy-Connection");
		
		if ( keepAliveHeader!=null ) {
			return "close".equals( keepAliveHeader.getValue().trim() );
		}
		
		return false;
	}

	public static boolean isKeepAlive(HttpMessage httpMessage) {
		Header keepAliveHeader = httpMessage.getFirstHeader("Keep-Alive");
		
		if ( keepAliveHeader!=null ) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isConnectionClose(HttpMessage httpMessage) {
		Header keepAliveHeader = httpMessage.getFirstHeader("Connection");
		
		if ( keepAliveHeader!=null ) {
			return "close".equals( keepAliveHeader.getValue().trim() );
		}
		
		return false;
	}
	
	public static boolean isConnectionKeepAlive(HttpMessage httpMessage) {
		Header keepAliveHeader = httpMessage.getFirstHeader("Connection");
		
		if ( keepAliveHeader!=null ) {
			return "keep-alive".equals( keepAliveHeader.getValue().trim() );
		}
		
		return false;
	}
	
	public static void addConnectionClose(HttpMessage httpMessage) {
		httpMessage.removeHeader( httpMessage.getFirstHeader("Connection") );
		httpMessage.addHeader(new BasicHeader("Connection", "close"));
	}
	
	public static void addConnectionKeepAlive(HttpMessage httpMessage) {
		httpMessage.removeHeader( httpMessage.getFirstHeader("Connection") );
		httpMessage.addHeader(new BasicHeader("Connection", "keep-alive"));
	}
	
	/**
	 * Check if an HTTP response or request will contain body data
	 * 
	 * @param httpMessage
	 * @return
	 */
	public static boolean hasBody(HttpMessage httpMessage) {
		boolean hasBody = false;
		
		Header contentLengthHeader = httpMessage.getFirstHeader("Content-Length");
		if (contentLengthHeader != null) {
			String contentLengthString = contentLengthHeader.getValue().trim();
			Long contentLength = Long.valueOf(contentLengthString);
			
			if ( contentLength > 0 ) {
				hasBody = true;
			}
		} 
		
		Header contentTypeHeader = httpMessage.getFirstHeader("Content-Type");
		Header connectionHeader = httpMessage.getFirstHeader("Connection");
		if (contentTypeHeader != null && connectionHeader != null) {
			if (connectionHeader.getValue().equals("close")) {
				hasBody = true;
			}
		} 
		
		Header contentEncodingHeader = httpMessage.getFirstHeader("Transfer-Encoding");
		if (contentEncodingHeader != null) {
			String contentEncodingString = contentEncodingHeader.getValue().trim();

			if ( contentEncodingString.equals("chunked") ) {
				hasBody = true;
			}
		}

		return hasBody;
	}	

}
