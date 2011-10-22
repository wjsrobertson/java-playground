package net.xylophones.megaproxy;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import junit.framework.TestCase;

import net.xylophones.megaproxy.io.HttpInputStream;

import org.apache.http.HttpMessage;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.log4j.Logger;

public class HttpInputStreamTest extends TestCase  {
	
	private static final Logger log = Logger.getLogger(HttpInputStreamTest.class);

	public void testRequest() throws IOException {
		StringBuilder data = new StringBuilder();
		data.append("HTTP/1.1 404 Not Found\r\n");
		data.append("Header1: value1\r\n");
		data.append("Header2: value2\r\n");
		data.append("\r\n");
		data.append("body here");
		
		testHttpStreamHandler(data.toString());
	}
	
	public void testResponse() throws IOException {
		StringBuilder data = new StringBuilder();
		data.append("GET /httpcomponents-core/httpcore/apidocs/stylesheet.css HTTP/1.1\r\n");
		data.append("Header1: value1\r\n");
		data.append("Header2: value2\r\n");
		data.append("\r\n");
		data.append("body here");
		
		testHttpStreamHandler(data.toString());
	}
	
	public void testHttpStreamHandler(String data) throws IOException {	
		ByteArrayInputStream is = new ByteArrayInputStream( data.getBytes() );
		HttpInputStream his = new HttpInputStream(is);
		
		HttpMessage httpMessage = his.readHttpMessage();
		log.debug(httpMessage.getFirstHeader("Header1").getValue());
		assertEquals( "value1", httpMessage.getFirstHeader("Header1").getValue() );
		assertEquals( "value2", httpMessage.getFirstHeader("Header2").getValue() );
		
		ByteArrayBuffer inputBuffer   = new ByteArrayBuffer(1024);
		int bytesRead;
		byte[] buffer = new byte[56];
		while ((bytesRead = his.read(buffer)) != -1) {
			log.debug("read " + bytesRead + " bytes" );
			inputBuffer.append(buffer, 0, bytesRead);
		}
		
		String body = new String( inputBuffer.toByteArray() );
		log.debug("body: '" + body + "'" );
		log.debug("body length: " + body.length() );
		assertEquals("body here", body);
	
	}
	
}
