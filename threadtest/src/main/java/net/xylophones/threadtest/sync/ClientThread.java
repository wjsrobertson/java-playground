package net.xylophones.threadtest.sync;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread {

	private Socket socket;
	
	private boolean running = true;

	private volatile Model model = new Model();
	
	private Command command = new Command();
	
	private DataInputStream dataInputStream;
	
	private DataOutputStream dataOutputStream;
	
	private void process() throws IOException {
		while (running) {
			Socket socket = new Socket("127.0.0.1", 1024);

			dataInputStream = new DataInputStream( socket.getInputStream() );
			dataOutputStream = new DataOutputStream( socket.getOutputStream() );

			//Console console = System.console();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			
			String line = null;
			while ( true ) {
				line=reader.readLine();
				
				if (line == null) {
					continue;
				}
				if ( line.equals("quit") ) {
					running = false;
					break;
				}
				
				int code = Integer.valueOf(line);
				System.out.println("Sending: " + code);
				dataOutputStream.writeInt(code);
				if (code == MasterThread.COMMAND_RETRIEVE) {
					model.fromInputStream(dataInputStream);
					System.out.println("Got: " + model);
				}
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		ClientThread ct = new ClientThread();
		ct.process();
	}

}
