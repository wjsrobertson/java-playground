package net.xylophones.midifinder.runner;

import net.xylophones.midifinder.http.Retriever;
import net.xylophones.midifinder.model.Url;
import net.xylophones.midifinder.service.jpa.JpaUrlService;
import net.xylophones.midifinder.util.ChildUrlParser;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Callable;

public class HtmlUrlChildrenTask implements Callable<Boolean> {
    
    private static final Logger log = Logger.getLogger(HtmlUrlChildrenTask.class);

    private final JpaUrlService jpaUrlService;
    
	private final Retriever retriever;

	private final ChildUrlParser childUrlParser;
    
    private final Url url;

    public HtmlUrlChildrenTask(JpaUrlService jpaUrlService, Retriever retriever, ChildUrlParser childUrlParser, Url url) {
        this.jpaUrlService = jpaUrlService;
        this.retriever = retriever;
        this.childUrlParser = childUrlParser;
        this.url = url;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            log.debug("Starting process of fetching children of '" + url + "'");

            byte[] body = null;
            try {
                body = retriever.getBody(url.getFullUrl());
            } catch (IOException e) {
                jpaUrlService.markRequestFailed(url);
                return false;
            }

            Set<Url> childUrls = childUrlParser.createChildUrls(body, url);
            jpaUrlService.saveUrlsAndMarkUrlRetrieved(childUrls, url);

            log.debug(childUrls.size() + " children of '" + url + "' found");
            return true;
        } catch (Exception e) {
            log.warn("Fetching children of '" + url + "' failed - " + e.getMessage(), e);
            jpaUrlService.markRequestFailed(url);
            return false;
        }
    }
}
