package net.xylophones.midifinder.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;
import org.w3c.tidy.Tidy;

public class JTidyHtmlTidier implements HtmlTidier {

	private static final Logger log = Logger.getLogger(JTidyHtmlTidier.class);

	@Override
	public byte[] tidy(byte[] html) {

		if (log.isDebugEnabled()) {
			log.debug("Original HTML ("+html.length+" bytes): \n" + new String(html) );
		}

		Tidy tidy = new Tidy();
		tidy.setTabsize(4);
		//tidy.setBreakBeforeBR(true);
		//tidy.setIndentAttributes(true);
		tidy.setQuiet(true);
		tidy.setIndentContent(true);
		//tidy.setOnlyErrors(true);
		tidy.setMakeClean(true);
		//tidy.setMakeBare(true);
		//tidy.setWord2000(true);
		tidy.setXmlOut(true);
		tidy.setXmlSpace(true);
		// tidy.setForceOutput(true); // add me back when using new jar
		tidy.setXmlTags(false);
		tidy.setDocType("omit");
		//tidy.setPrintBodyOnly(true);
		//tidy.setXHTML(false);
		//tidy.setFixUri(true);
		
		ByteArrayInputStream in = new ByteArrayInputStream(html);
		ByteArrayOutputStream out = new ByteArrayOutputStream();	
		tidy.parse(in, out);
				
		byte[] tidyHtml = out.toByteArray();
		
		if (log.isDebugEnabled()) {
			log.debug("Tidy HTML ("+tidyHtml.length+" bytes): \n" + new String(tidyHtml) );
		}
		
		return tidyHtml;
	}
	
}
