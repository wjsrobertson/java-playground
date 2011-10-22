package net.xylophones.megaproxy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import net.xylophones.megaproxy.io.HttpOutputStream;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.log4j.Logger;

public class HttpOutputStreamTest extends TestCase {
	
	private static final Logger log = Logger.getLogger(HttpOutputStreamTest.class);

	public void testResponse() throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				
		HttpOutputStream httpOutputStream = new HttpOutputStream(byteArrayOutputStream);
		
		HttpVersion httpVersion = new HttpVersion(1, 1);
		StatusLine statusLine = new BasicStatusLine(httpVersion, 200, "OK");
		HttpResponse httpResponse = new BasicHttpResponse(statusLine);
		
		BasicHeader header = new BasicHeader("Test", "Value");
		httpResponse.addHeader(header);
		
		httpOutputStream.writeHttpMessage(httpResponse);
		
		byte[] data = byteArrayOutputStream.toByteArray();
		String written = new String(data);
		
		log.debug(written);
		
		assertNotNull(written);
		assertTrue(written.length() > 0);
		assertEquals("HTTP/1.1 200 OK\r\nTest: Value\r\n\r\n", written);		
	}
	
	public void testRequest() throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				
		HttpOutputStream httpOutputStream = new HttpOutputStream(byteArrayOutputStream);
		
		HttpVersion httpVersion = new HttpVersion(1, 0);
		HttpRequest httpRequest = new BasicHttpRequest("GET", "http://example.com", httpVersion);
		BasicHeader header = new BasicHeader("Test", "Value");
		httpRequest.addHeader(header);
		
		httpOutputStream.writeHttpMessage(httpRequest);
		
		byte[] data = byteArrayOutputStream.toByteArray();
		String written = new String(data);
		
		log.debug(written);
		
		assertNotNull(written);
		assertTrue(written.length() > 0);
		assertEquals("GET http://example.com HTTP/1.0\r\nTest: Value\r\n\r\n", written);		
	}
}
