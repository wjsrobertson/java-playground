package net.xylophones.nio.examples.server;

import com.google.common.collect.Multimap;

import java.nio.ByteBuffer;

public class RequestContext {

    private int numBytesRead;

    ByteBuffer requestBuffer;

    private Multimap headers;

    private String method;

    private String path;

    public int getNumBytesRead() {
        return numBytesRead;
    }

    public void setNumBytesRead(int numBytesRead) {
        this.numBytesRead = numBytesRead;
    }

    public Multimap getHeaders() {
        return headers;
    }

    public void setHeaders(Multimap headers) {
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ByteBuffer getRequestBuffer() {
        return requestBuffer;
    }

    public void setRequestBuffer(ByteBuffer requestBuffer) {
        this.requestBuffer = requestBuffer;
    }
}
