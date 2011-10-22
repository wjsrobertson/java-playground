package net.xylophones.midifinder.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.xylophones.midifinder.model.Url;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class ChildUrlParser {

	private static final Logger log = Logger.getLogger(ChildUrlParser.class);
	
	private final HtmlTidier htmlTidier;
	
	private final XPathUrlParser urlParser;
	
	private final UrlCleaner urlCleaner;
	
	public ChildUrlParser(HtmlTidier htmlTidier,XPathUrlParser urlParser, UrlCleaner urlCleaner) {
		this.htmlTidier = htmlTidier;
		this.urlParser  = urlParser;
		this.urlCleaner = urlCleaner;
	}
	
	/**
	 * Create child URLs from a page
	 */
	public Set<Url> createChildUrls(byte[] pageData, Url referer) {
		byte[] htmlData = htmlTidier.tidy(pageData);
		Set<Url> childUrls = new LinkedHashSet<Url>();
		Map<String,String> urls = urlParser.parse(htmlData);
		
		for (Map.Entry<String,String> urlEntry: urls.entrySet()) {
			String url  = urlEntry.getKey();
			String text = urlEntry.getValue();
			
			if (url.matches("^\\w+:\\w.*")) {
				// we dont' want mailto: javascript: etc.
				continue;
			}
			if (url.matches("^#.*")) {
				// we don't want these
				continue;
			}
			
			Url newUrl = new Url();
			
			if (text != null && ! text.equals("")) {
				newUrl.setAnchorText(text);
			}
			
			if (url.contains("://") && StringUtils.countMatches(url, "/") == 2) {
				url = url + "/";
			}
			
			if (! url.contains("://")) {
				if ( url.startsWith("/") ) {
					url = referer.getScheme() + "://" + referer.getHost() + url;
				} else {
					String directory = "";
					if ( referer.getPath().endsWith("/") ) {
						directory = referer.getPath();
					} else {
						directory = StringUtils.substringBeforeLast(referer.getPath(), "/") + "/";
					}
					if (! directory.startsWith("/")) {
						directory = "/" + directory;
					}
					
					url = referer.getScheme() + "://" + referer.getHost() + directory  + url;
				}
			}
			
			URL parsedURL = null;
			try {
				parsedURL = new URL(url);
			} catch (MalformedURLException e) {
				log.warn("Couldn't parse: '" + url +"'");
				continue;
			}

			newUrl.setParent(referer);
			newUrl.setPath( parsedURL.getPath() );
			newUrl.setQuery( parsedURL.getQuery() );
			newUrl.setHost( parsedURL.getHost() );
			newUrl.setScheme( parsedURL.getProtocol() );
			
			if ( parsedURL.getPath() == null || ! parsedURL.getPath().startsWith("/")  ) {
				newUrl.setPath( referer.getPath() );
			}
			
			newUrl.setPath( urlCleaner.cleanPath( newUrl.getPath() ));
	
			childUrls.add(newUrl);
		}
		
		return childUrls;
	}	
}
