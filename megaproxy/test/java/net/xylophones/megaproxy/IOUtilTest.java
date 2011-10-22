package net.xylophones.megaproxy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.xylophones.megaproxy.io.IOUtil;

import junit.framework.TestCase;

public class IOUtilTest extends TestCase {

	public void testCopy() throws IOException {
		
		String testString = "abcdefghijklmnopqrstuvwxyz";
		
		ByteArrayInputStream bais = new ByteArrayInputStream(testString.getBytes());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		
		
		IOUtil.copy(bais, baos, 5);
		
		String test = new String( baos.toByteArray() );
		assertEquals("abcde", test);
	}
	
}
