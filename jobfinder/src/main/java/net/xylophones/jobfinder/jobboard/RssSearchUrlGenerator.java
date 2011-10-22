package net.xylophones.jobfinder.jobboard;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.stereotype.Component;

@Component("rssSearchUrlGenerator")
public class RssSearchUrlGenerator {

	public String generateUrl(int page, String searchTerm) {
		searchTerm = searchTerm.toUpperCase();
		try {
			searchTerm = URLEncoder.encode(searchTerm, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}

		String url = "http://www.theitjobboard.co.uk" +
					 "/rss/" +
					 searchTerm +
					 "/LONDON/en/jobs-feed.xml?" +
					 "JobTypeFilter=1&DatePostedFilter=0&Page=" +
					 page +
					 "&OrderBy=0&CountryId=0";

		return url;
	}
}
