package net.xylophones.midifinder.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.ccil.cowan.tagsoup.Parser;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

@Component("htmlTidier")
public class TagSoupHtmlTidier implements HtmlTidier {

	@Override
	public byte[] tidy(byte[] html)  {

		ByteArrayOutputStream bos = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(html);
			XMLReader reader = new Parser();
			reader.setFeature(Parser.namespacesFeature, false);
			reader.setFeature(Parser.namespacePrefixesFeature, false);
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
	
			DOMResult result = new DOMResult();
			transformer.transform(new SAXSource(reader, new InputSource(bis)), result);
	
			Document doc = (Document) result.getNode();
			
			OutputFormat format = new OutputFormat(doc);
			format.setIndenting(true);
			bos = new ByteArrayOutputStream();
			XMLSerializer serializer = new XMLSerializer(bos, format);
			serializer.serialize(doc);
			
		} catch (IOException e) {
			throw new HtmlTidyException(e);
		} catch (SAXNotSupportedException e) {
			throw new HtmlTidyException(e);
		} catch (TransformerException e) {
			throw new HtmlTidyException(e);
		} catch (TransformerFactoryConfigurationError e) {
			throw new HtmlTidyException(e);
		} catch (SAXNotRecognizedException e) {
			throw new HtmlTidyException(e);
		}

		return bos.toByteArray();
	}

}
