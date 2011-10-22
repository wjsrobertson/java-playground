package net.xylophones.megaproxy.io;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.Header;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.log4j.Logger;

public class HttpOutputStream extends OutputStream {
	
	private static final Logger log = Logger.getLogger(HttpOutputStream.class);
	
	final OutputStream outputStream;
	
	public HttpOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	/**
	 * Write an HttpMessage to the underlying {@see OutputStream}
	 * 
	 * @param httpMessage
	 * @throws IOException
	 */
	public void writeHttpMessage(HttpMessage httpMessage) throws IOException {
		if (httpMessage instanceof HttpResponse) {
			HttpResponse httpResponse = (HttpResponse) httpMessage;
			StatusLine statusLine = httpResponse.getStatusLine();
			write( statusLine.toString().getBytes() );
			write( "\r\n".getBytes() );
			
			log.debug("wrote status line:- " + statusLine);
		} else if (httpMessage instanceof HttpRequest) {
			HttpRequest httpRequest = (HttpRequest) httpMessage;
			RequestLine requestLine = httpRequest.getRequestLine();
			write( requestLine.toString().getBytes() );
			write( "\r\n".getBytes() );
			
			log.debug("wrote request line:- " + requestLine);
		} else {
			throw new IllegalArgumentException("Illegal argument");
		}
		
		for ( Header header: httpMessage.getAllHeaders() ) {
			String name = header.getName();
			write( name.getBytes() );
			write( ": ".getBytes() );
			String value = header.getValue();
			write( value.getBytes() );
			write( "\r\n".getBytes() );
			
			log.debug("wrote header:- " + name + ": " + value );
			
		}
		
		write( "\r\n".getBytes() );
	}

	/**
	 * @throws IOException
	 * @see java.io.OutputStream#close()
	 */
	public void close() throws IOException {
		outputStream.close();
	}

	/**
	 * @throws IOException
	 * @see java.io.OutputStream#flush()
	 */
	public void flush() throws IOException {
		outputStream.flush();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws IOException
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	public void write(byte[] arg0, int arg1, int arg2) throws IOException {
		outputStream.write(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @throws IOException
	 * @see java.io.OutputStream#write(byte[])
	 */
	public void write(byte[] arg0) throws IOException {
		outputStream.write(arg0);
	}

	/**
	 * @param arg0
	 * @throws IOException
	 * @see java.io.OutputStream#write(int)
	 */
	public void write(int arg0) throws IOException {
		outputStream.write(arg0);
	}
	
	
	
}