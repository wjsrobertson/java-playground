package net.xylophones.threadtest.sync;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Model implements Sendable {

	private int myInteger = 0;
	
	private char myChar = 'a';

	public char getMyChar() {
		return myChar;
	}

	public void setMyChar(char myChar) {
		this.myChar = myChar;
	}

	public int getMyInteger() {
		return myInteger;
	}
	
	public void setMyInteger(int myInteger) {
		this.myInteger = myInteger;
	}

	public void fromInputStream(DataInputStream s) throws IOException {
		myInteger = s.readInt();
		myChar = s.readChar();
	}

	public void toOutputStream(DataOutputStream s) throws IOException {
		s.writeInt(myInteger);
		s.writeChar(myChar);
	}

}
