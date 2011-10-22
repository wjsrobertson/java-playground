package net.xylophones.threadtest.sync;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Sendable {

	public void fromInputStream(DataInputStream s) throws IOException;
	
	public void toOutputStream(DataOutputStream s) throws IOException;
	
}
