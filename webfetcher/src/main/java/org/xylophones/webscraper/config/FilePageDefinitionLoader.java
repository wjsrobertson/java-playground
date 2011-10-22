package org.xylophones.webscraper.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xylophones.webscraper.parser.DataType;
import org.xylophones.webscraper.parser.ParseComponent;

public class FilePageDefinitionLoader implements PageDefinitionLoader {
	
	private static final Log log = LogFactory.getLog(FilePageDefinitionLoader.class);
	
	private final Map<String,Set<ParseComponent>> pages = new ConcurrentHashMap<String,Set<ParseComponent>>();
	
	public FilePageDefinitionLoader() {
	}
	
	public FilePageDefinitionLoader(String filePath) {
		init(filePath);
	}
	
	public void init(String filePath) {
		if (filePath == null) {
			return;
		}
		
		InputStream input = null;
		if ( filePath.matches("^classpath:.*") ) {
			filePath = filePath.replace("classpath:", "");
			input = FilePageDefinitionLoader.class.getResourceAsStream(filePath);
		} else {
			try {
				input = new FileInputStream(filePath);
			} catch (FileNotFoundException e) {
				return;
			}
		}
		
		/*
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			byte[] read = new byte[1024];	
			int readLength = -1;
			int total = 0;
			while ( (readLength=input.read(read)) != -1 ) {
				output.write( read, total, readLength  );
				total += readLength;
			}
		} catch (IOException e) {
			log.error(e);
			return;
		}
		String fileString = new String(output.toByteArray());
		*/
		
		try {
	        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
	        Document doc = docBuilder.parse(input);
	        NodeList pageNodes = doc.getElementsByTagName("page");
	        if (pageNodes == null) {
	        	return;
	        }
	        for (int i=0 ; i<pageNodes.getLength() ; i++) {
	        	Node page = pageNodes.item(i);
	        	NamedNodeMap attributes = page.getAttributes();
	        	Node nameItem = attributes.getNamedItem("name");
	        	if (nameItem  == null) {
	        		throw new RuntimeException("name for page is absent");
	        	}
	        	String name = nameItem.getTextContent();
	        	if (name  == null || name.length() == 0) {
	        		throw new RuntimeException("name for page is empty");
	        	}
	        	
	        	Set<ParseComponent> expressions = new CopyOnWriteArraySet<ParseComponent>();
	        	pages.put(name, expressions);
	        	
	        	NodeList childNodes = page.getChildNodes();

		        for (int j=0 ; j<childNodes.getLength() ; j++) {
		        	Node item = childNodes.item(j);
		        	
		        	String nodeName = item.getNodeName();
		        	String localName = item.getLocalName();
		        	
					//if ("expression".equals(nodeName)) {
		        		NamedNodeMap expressionAttributes = item.getAttributes();
		        		if ( expressionAttributes == null ) {
		        			//log.warn("empty expression tag");
		        			continue;
		        		}
		        		
			        	Node typeItem = expressionAttributes.getNamedItem("type");
			        	if (typeItem  == null) {
			        		throw new RuntimeException("type for expression is absent");
			        	}
			        	String type= typeItem.getTextContent();
			        	if (type  == null || type.length() == 0) {
			        		throw new RuntimeException("type for expression is empty");
			        	}
			        	
			        	Node valueItem = expressionAttributes.getNamedItem("value");
			        	if (valueItem  == null) {
			        		throw new RuntimeException("value for expression is absent");
			        	}
			        	String value= valueItem.getTextContent();
			        	if (value  == null || value.length() == 0) {
			        		throw new RuntimeException("value for expression is empty");
			        	}
			        	
			        	Node expressionNameItem = expressionAttributes.getNamedItem("name");
			        	if (expressionNameItem  == null) {
			        		throw new RuntimeException("name for expression is absent");
			        	}
			        	String expressionName= expressionNameItem.getTextContent();
			        	if (expressionName  == null || name.length() == 0) {
			        		throw new RuntimeException("name for expression is empty");
			        	}
			        	
			        	expressions.add(
			        		new ParseComponent(expressionName, value, DataType.valueOf(type.toUpperCase()))
			        	);
		        	//}
		        }
	        }
	        
		} catch (Exception e) {
			log.error(e);
		}
	}

	public String[] getPageList() {
		return pages.keySet().toArray(new String[]{});
	}
	
	public Set<ParseComponent> getPageComponents(String name) {
		return pages.get(name);
	}
	
	
	
	
}
