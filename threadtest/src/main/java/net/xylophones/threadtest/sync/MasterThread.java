package net.xylophones.threadtest.sync;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;

public class MasterThread {
	
	private ServerSocket serverSocket;
	
	private boolean running = true;

	private volatile Model model = new Model();
	
	public static final int COMMAND_RETRIEVE = 1;

	public MasterThread() throws IOException  {
		serverSocket = new ServerSocket(1024);
	}
	
	private void process() throws IOException {
		while (running) {
			Socket socket = serverSocket.accept();
			SocketProcessor processor = new SocketProcessor(socket);
			processor.process();
		}
	}
	
	public static void main(String[] args) throws IOException {
		MasterThread mt = new MasterThread();
		mt.process();
	}
	
	/**
	 * Class dealing with client connection
	 */
	class SocketProcessor {

		private final DataInputStream dataInputStream;
		
		private final DataOutputStream dataOutputStream;
		
		public SocketProcessor(Socket socket) throws IOException {
			dataInputStream = new DataInputStream( socket.getInputStream() );
			dataOutputStream = new DataOutputStream( socket.getOutputStream() );
		}
		
		public void process() {
			Thread reciever = new Thread(new Receiver());
			reciever.start();
		}

		class Receiver implements Runnable {
			public void run() {
				Command command = new Command();
				while (running) {
					try {
						System.out.println("Populating from input stream");
						
						command.fromInputStream( dataInputStream );
						System.out.println("Got command: " + command.getCommand());
						
						switch ( command.getCommand() ) {
							case COMMAND_RETRIEVE:
								model.toOutputStream(dataOutputStream);
							default:
								break;
						}
					} catch (IOException e) {
						System.err.println("IOException: " + e);
						break;
					}
				} 
			}
		}
	}
	
}
