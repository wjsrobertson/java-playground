package net.xylophones.midifinder.util;

public interface HtmlTidier {

	/**
	 * Tidy the HTML of a page
	 * 
	 * @param html
	 * @return
	 */
	public abstract byte[] tidy(byte[] html);

}