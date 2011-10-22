package net.xylophones.jobfinder.jobboard;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Component("rssParser")
public class RssParser {

	private ThreadLocal<XPath> xPath = new ThreadLocal<XPath>() {
		protected XPath initialValue() {
			XPathFactory factory = XPathFactory.newInstance();
			return factory.newXPath();
		}
	};
	
	private XPath getXPath() {
		return xPath.get();
	}

	public List<RssItem> parseJobsFromRss(String rss) {
		List<RssItem> rssItems = new ArrayList<RssItem>();

		InputSource inputSource = new InputSource( new ByteArrayInputStream(rss.getBytes()) );
		inputSource.setEncoding("UTF-8");
		
		NodeList jobNodes = getJobNodes(inputSource);
		for (int i=0 ; i<jobNodes.getLength() ; i++) {
			Node jobNode = jobNodes.item(i);
			
			RssItem rssItem = new RssItem();
			
			String title = getStringFromPath(jobNode, "title");
			rssItem.setTitle(title);
			
			String description = getStringFromPath(jobNode, "description");
			rssItem.setDescription(description);
			
			String publicationDate = getStringFromPath(jobNode, "publicationDate");
			rssItem.setPublicationDate(publicationDate);
			
			String link = getStringFromPath(jobNode, "link");
			rssItem.setLink(link);
			
			rssItems.add(rssItem);
		}
		
		return rssItems;
	}
	
	private NodeList getJobNodes(InputSource inputSource) {
		try {
			return (NodeList) getXPath().evaluate("/rss/channel/item",inputSource, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}
	
	private String getStringFromPath(Node jobNode, String path) {
		try {
			return (String) getXPath().evaluate(path,jobNode, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}
	
}
