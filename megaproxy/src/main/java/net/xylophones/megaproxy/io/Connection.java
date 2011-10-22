package net.xylophones.megaproxy.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {
	
	private final HttpOutputStream outputStream;
	
	private final HttpInputStream inputStream;
	
	private final Socket socket; 
	
	public Connection(Socket socket) throws IOException {
		outputStream = new HttpOutputStream(
			new BufferedOutputStream( socket.getOutputStream() )
		);
		
		inputStream = new HttpInputStream(
			new BufferedInputStream( socket.getInputStream() )
		);
		
		this.socket = socket;
	}

	public Socket getSocket() {
		return socket;
	}

	public HttpOutputStream getOutputStream() {
		return outputStream;
	}

	public HttpInputStream getInputStream() {
		return inputStream;
	}
	
	public void close() throws IOException {
		if (socket.isConnected() && ! socket.isClosed()) {
			socket.close();
		}
	}


}
