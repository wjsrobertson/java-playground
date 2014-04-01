package net.xylophones.nio.examples.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicLong;

public class AsynchronousChannelCopier {

    final AsynchronousFileChannel readFileChannel;
    final AsynchronousFileChannel writeFileChannel;
    final ByteBuffer copyBuffer;
    final AtomicLong positionContainer;

    final CompletionHandler<Integer, Void> fileReadCompletionHandler = new CompletionHandler<Integer, Void>() {
        public void completed(Integer numBytesRead, Void empty) {
            if (numBytesRead != -1) {
                System.out.println("Read " + numBytesRead + " bytes");
                copyBuffer.flip();
                write(numBytesRead);
            } else {
                System.out.println("No bytes read");
            }
        }

        public void failed(Throwable exc, Void empty) {
            System.err.println("reading failed: " + exc.getMessage());
        }
    };

    final CompletionHandler<Integer, Integer> fileWriteCompletionHandler = new CompletionHandler<Integer, Integer>() {
        public void completed(Integer numBytesWritten, Integer numBytesRead) {
            if (numBytesWritten != -1) {
                System.out.println("Wrote " + numBytesWritten + " bytes");
                positionContainer.addAndGet(numBytesRead);
                copyBuffer.flip();
                copyBuffer.position(0);
                read();
            }
        }

        public void failed(Throwable exc, Integer numBytesRead) {
            System.err.println("reading failed: " + exc.getMessage());
        }
    };

    public AsynchronousChannelCopier(final AsynchronousFileChannel from, final AsynchronousFileChannel to, int bufferSize) {
        readFileChannel = from;
        writeFileChannel = to;
        copyBuffer = ByteBuffer.allocate(bufferSize);
        positionContainer = new AtomicLong(0);
    }

    public void copy() {
        read();
    }

    public void read() {
        readFileChannel.read(copyBuffer, positionContainer.get(), null, fileReadCompletionHandler);
    }

    public void write(Integer numBytesRead) {
        writeFileChannel.write(copyBuffer, positionContainer.get(), numBytesRead, fileWriteCompletionHandler);
    }

}
