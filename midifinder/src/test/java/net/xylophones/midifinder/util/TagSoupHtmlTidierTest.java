package net.xylophones.midifinder.util;

import junit.framework.TestCase;

public class TagSoupHtmlTidierTest extends TestCase {

	public void testX() {
		TagSoupHtmlTidier tidier = new TagSoupHtmlTidier();
		
		StringBuilder builder = new StringBuilder();
		builder.append("<html><a>banana<div></div></html>");
		
		byte[] tidy = tidier.tidy( builder.toString().getBytes() );
		
		System.out.println(new String(tidy));
	}
	
}
