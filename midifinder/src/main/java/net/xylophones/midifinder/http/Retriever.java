package net.xylophones.midifinder.http;

import java.io.IOException;

import org.apache.commons.httpclient.methods.HeadMethod;

public interface Retriever {

	public abstract HeadMethod getHead(String url) throws IOException;

	public abstract byte[] getBody(String url) throws IOException;

}