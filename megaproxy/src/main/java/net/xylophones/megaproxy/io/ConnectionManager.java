package net.xylophones.megaproxy.io;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectionManager {
	
	private final Log log = LogFactory.getLog(ConnectionManager.class);
	
	private final Map<String, Connection> connections = new HashMap<String, Connection>();

	public synchronized Connection getConnection(URL url) throws IOException {
		int port = (url.getPort() > 0) ? url.getPort() : 80;		
		String id = url.getHost() + ":" + port;
		
		if ( connections.containsKey(id) ) {
			log.debug("returning existing connection for " + id);
			Connection connection = connections.get(id);
			
			if ( connection.getSocket().isClosed() ) {
				connection = createConnection(url.getHost(), port);
				connections.put(id, connection);
			}
			
			return connection;
		}
		
		log.debug("returning new connection for " + id);

		Connection conection = createConnection(url.getHost(), port);
		connections.put(id, conection);
		
		return conection;
	}
	
	private Connection createConnection(String host, int port) throws IOException {
		Socket socket = new Socket(host, port);
		Connection conection = new Connection(socket);
		
		return conection;
	}
}
