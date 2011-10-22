package net.xylophones.megaproxy.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import org.apache.http.HttpMessage;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.log4j.Logger;

public class HttpInputStream extends InputStream {

	private final PushbackInputStream inputStream;
	
	private static final int READ_SIZE = 1024; 
	
	private final byte[] buffer = new byte[READ_SIZE];
	
	private static final Logger log = Logger.getLogger(HttpInputStream.class);
	
	private static boolean headersComplete = false;
	
	private HttpMessage httpMessage;
	
	public HttpInputStream(InputStream inputStream) {
		this.inputStream = new PushbackInputStream(inputStream, READ_SIZE);
	}
	
	protected BasicHttpResponse createBasicHttpResponse(String line) {
		int firstSpace    = line.indexOf(" ");
		int secondSpace   = line.indexOf(" ", firstSpace+1);
		
		String protocol   = line.substring(0, firstSpace);
		String codeString = line.substring(firstSpace+1, secondSpace);
		String message    = line.substring(secondSpace+1);
		
		//String protocolName   = protocol.substring(0, protocol.indexOf("/"));
		String majorVerString = protocol.substring(protocol.indexOf("/")+1, protocol.indexOf("."));
		String minorVerString = protocol.substring(protocol.indexOf(".")+1);

		HttpVersion httpVersion = new HttpVersion(Integer.valueOf(majorVerString), Integer.valueOf(minorVerString));
		StatusLine statusLine = new BasicStatusLine(httpVersion, Integer.valueOf(codeString), message);
		return new BasicHttpResponse(statusLine);
	}
	
	protected BasicHttpEntityEnclosingRequest createBasicHttpRequest(String line) {
		int firstSpace  = line.indexOf(" ");
		int secondSpace = line.indexOf(" ", firstSpace+1);
		
		String method   = line.substring(0, firstSpace);
		String uri      = line.substring(firstSpace+1, secondSpace);
		String protocol = line.substring(secondSpace+1);
		
		//String protocolName   = protocol.substring(0, protocol.indexOf("/"));
		String majorVerString = protocol.substring(protocol.indexOf("/")+1, protocol.indexOf("."));
		String minorVerString = protocol.substring(protocol.indexOf(".")+1);

		HttpVersion httpVersion = new HttpVersion(Integer.valueOf(majorVerString), Integer.valueOf(minorVerString));

		return new BasicHttpEntityEnclosingRequest(method, uri, httpVersion);
	}

	/**
	 * Read an HTTP message from the stream, stopping when headers are read
	 * 
	 * @return
	 * @throws IOException
	 */
	public HttpMessage readHttpMessage() throws IOException {
		final ByteArrayBuffer inputBuffer   = new ByteArrayBuffer(1024);
		final LineEndWatcher sectionWatcher = new LineEndWatcher(); 
		int lineNum = 0;
		int bytesRead;
		HttpMessage httpMessage = null;

		HEADER_LOOP:
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			for ( int i=0 ; i<bytesRead ; i++ ) {
				sectionWatcher.addByte(buffer[i]);
				
				if ( buffer[i] == '\n' ) {
					lineNum++;
					if ( sectionWatcher.isHeaderSectionEnd() ) {
						log.debug("Headers section has ended");
						
						if (bytesRead > 4) {	// length of \r\n\r\n
							inputStream.unread(buffer, i+1, bytesRead-i-1);
						}
						break HEADER_LOOP;
					} else {
						String line = new String( inputBuffer.toByteArray() ).trim();
						if (lineNum == 1) {
							if (log.isDebugEnabled()) {		
								log.debug("Read request/response line:- " + line);
							}
							
							if ( line.startsWith("HTTP") ) {
								httpMessage = createBasicHttpResponse(line);
							} else {
								httpMessage = createBasicHttpRequest(line);
							}
						} else {
							if (log.isDebugEnabled()) {							
								log.debug("Read header:- " + line);
							}
							
							String name = line.substring(0, line.indexOf(":"));
							String value = line.substring(line.indexOf(":")+1);
							value = value.trim();
							/*
							if (value.startsWith(" ")) {
								value = value.substring(value.lastIndexOf(" ")+1);
							}
							if (value.startsWith("\t")) {
								value = value.substring(value.lastIndexOf("\t")+1);
							}
							*/
							BasicHeader header = new BasicHeader(name, value);
							httpMessage.addHeader(header);
						}
						
						inputBuffer.clear();
					}
				} else {
					inputBuffer.append( buffer, i, 1 );
				}
			}
		}
		
		return httpMessage;
	}

	/**
	 * @return
	 * @throws IOException
	 * @see java.io.InputStream#available()
	 */
	public int available() throws IOException {
		return inputStream.available();
	}

	/**
	 * @throws IOException
	 * @see java.io.InputStream#close()
	 */
	public void close() throws IOException {
		inputStream.close();
	}

	/**
	 * @param readlimit
	 * @see java.io.InputStream#mark(int)
	 */
	public void mark(int readlimit) {
		inputStream.mark(readlimit);
	}

	/**
	 * @return
	 * @see java.io.InputStream#markSupported()
	 */
	public boolean markSupported() {
		return inputStream.markSupported();
	}

	/**
	 * @return
	 * @throws IOException
	 * @see java.io.InputStream#read()
	 */
	public int read() throws IOException {
		return inputStream.read();
	}

	/**
	 * @param b
	 * @param off
	 * @param len
	 * @return
	 * @throws IOException
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
	public int read(byte[] b, int off, int len) throws IOException {
		return inputStream.read(b, off, len);
	}

	/**
	 * @param b
	 * @return
	 * @throws IOException
	 * @see java.io.InputStream#read(byte[])
	 */
	public int read(byte[] b) throws IOException {
		return inputStream.read(b);
	}

	/**
	 * @throws IOException
	 * @see java.io.InputStream#reset()
	 */
	public void reset() throws IOException {
		inputStream.reset();
	}

	/**
	 * @param n
	 * @return
	 * @throws IOException
	 * @see java.io.InputStream#skip(long)
	 */
	public long skip(long n) throws IOException {
		return inputStream.skip(n);
	}
	
	/**
	 * Class to watch for the end of headers
	 */
	private static class LineEndWatcher {
		
		byte[] bytes;
		
		int size;
		
		public LineEndWatcher() {
			this(4);
		}
		
		private LineEndWatcher(final int size) {
			bytes = new byte[size];
			this.size = size;
		}
		
		public void addByte(byte b) {
			for ( int i=1 ; i<size ; i++ ) {
				bytes[i-1] = bytes[i];
			}
			bytes[size-1] = b;
		}

		public boolean isHeaderSectionEnd() {
			return /*(bytes[size-1] == '\n' && bytes[size-2] == '\n') // should we tolerate this?
			       || */
			       (
			    		   (bytes[size-1] == '\n' && bytes[size-2] == '\r') 
			    	    && (bytes[size-3] == '\n' && bytes[size-4] == '\r')
			       );
		}
	}
}
