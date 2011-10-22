package net.xylophones.threadtest.sync;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Command implements Sendable {
	
	private int command;

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public void fromInputStream(DataInputStream s) throws IOException {
		command = s.readInt();
	}

	public void toOutputStream(DataOutputStream s) throws IOException {
		s.writeInt(command);
	}

}
