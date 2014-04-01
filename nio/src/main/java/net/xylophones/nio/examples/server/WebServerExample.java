package net.xylophones.nio.examples.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class WebServerExample {

    AsynchronousServerSocketChannel listener;
    private final Charset charset = Charset.forName("ASCII");
    private final CharsetEncoder encoder = charset.newEncoder();
    private final CharsetDecoder decoder = charset.newDecoder();

    public void start() throws IOException {
        listener = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8080));

        CompletionHandler<AsynchronousSocketChannel, RequestContext> acceptedCompletionHandler = new CompletionHandler<AsynchronousSocketChannel, RequestContext>() {
            public void completed(AsynchronousSocketChannel channel, RequestContext requestContext) {
                listener.accept(new RequestContext(), this);
                handleRequest(channel, requestContext);
            }

            public void failed(Throwable exc, RequestContext att) {
                listener.accept(new RequestContext(), this);
                System.err.println("listening failed: " + exc.getMessage());
            }
        };

        listener.accept(new RequestContext(), acceptedCompletionHandler);
    }

    private void handleRequest(final AsynchronousSocketChannel channel, RequestContext requestContext) {
        CompletionHandler<Integer, RequestContext> requestReadCompletionHandler = new CompletionHandler<Integer, RequestContext>() {
            public void completed(Integer numBytesRead, RequestContext requestContext) {
                if (numBytesRead != -1) {
                    System.out.println("Read " + numBytesRead + " bytes");
                    handleResponse(channel, requestContext);
                } else {
                    // read some more
                }
            }

            public void failed(Throwable exc, RequestContext att) {
                System.err.println("reading failed: " + exc.getMessage());
            }
        };

        ByteBuffer requestBuffer = ByteBuffer.allocate(1024);
        requestContext.setRequestBuffer(requestBuffer);
        channel.read(requestBuffer, requestContext, requestReadCompletionHandler);
    }

    private void handleResponse(final AsynchronousSocketChannel channel, RequestContext requestContext) {
        CompletionHandler <Integer, RequestContext> responseWriteCompletionHandler = new CompletionHandler<Integer, RequestContext>() {
            public void completed(Integer numBytesWritten, RequestContext requestContext) {
                if (numBytesWritten != -1) {
                    System.out.println("Wrote " + numBytesWritten + " bytes");
                    try {
                        channel.close();
                    } catch (IOException e) {
                        System.err.println("error closing: " + e.getMessage());
                    }
                } else {

                }
            }

            public void failed(Throwable exc, RequestContext att) {
                System.err.println("writing failed: " + exc.getMessage());
            }
        };

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String responseBody = "Hello";
        try {
            long contentLength = (encodeString(responseBody)).array().length;
            buffer.put(encodeString("HTTP/1.1 200 OK\r\n"));
            buffer.put(encodeString("Content-Type: text/plain\r\n"));
            buffer.put(encodeString("Content-Length: " + contentLength + "\r\n\r\n"));
            buffer.put(encodeString(responseBody));
        } catch (CharacterCodingException e) {
            System.err.println("encoding");
        }

        buffer.flip();
        channel.write(buffer, null, responseWriteCompletionHandler);
    }

    private ByteBuffer encodeString(String responseBody) throws CharacterCodingException {
        return encoder.encode(CharBuffer.wrap(responseBody));
    }
}
