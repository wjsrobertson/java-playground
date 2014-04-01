package net.xylophones.nio.examples;

import net.xylophones.nio.examples.server.RequestContext;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicLong;

public class AsyncronousFileCopyExample {

    public static void main(String[] args) throws IOException {
        String psthToReadFile = "D:\\Users\\Will\\Projects\\general\\nio\\src\\main\\resources\\sample_data.txt";
        String pathToWriteFile = "C:\\TEMP\\sample_data_copy.txt";

        AsyncronousFileCopyExample example = new AsyncronousFileCopyExample(Paths.get(psthToReadFile), Paths.get(pathToWriteFile));
        example.copy();

        try {
            Thread.sleep(1000 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    final AsynchronousFileChannel readFileChannel;
    final AsynchronousFileChannel writeFileChannel;
    final ByteBuffer copyBuffer = ByteBuffer.allocate(1024);
    final AtomicLong positionContainer = new AtomicLong(0);

    CompletionHandler<Integer, Void> fileReadCompletionHandler = new CompletionHandler<Integer, Void>() {
        public void completed(Integer numBytesRead, Void empty) {
            if (numBytesRead != -1) {
                System.out.println("Read " + numBytesRead + " bytes");
                copyBuffer.flip();
                write(numBytesRead);
            } else {
                try {
                    writeFileChannel.force(false);
                    writeFileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                System.out.println("No bytes read");
            }
        }

        public void failed(Throwable exc, Void empty) {
            System.err.println("reading failed: " + exc.getMessage());
        }
    };

    CompletionHandler<Integer, Integer> fileWriteCompletionHandler = new CompletionHandler<Integer, Integer>() {
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

    public AsyncronousFileCopyExample(Path from, Path to) throws IOException {
        readFileChannel = AsynchronousFileChannel.open(from, StandardOpenOption.READ);
        if (! Files.exists(to)) {
            Files.createFile(to);
        }
        writeFileChannel = AsynchronousFileChannel.open(to, StandardOpenOption.WRITE);
    }

    public void copy() throws IOException {
        writeFileChannel.truncate(0);
        read();
    }

    public void read() {
        readFileChannel.read(copyBuffer, positionContainer.get(), null, fileReadCompletionHandler);
    }

    public void write(Integer numBytesRead) {
        writeFileChannel.write(copyBuffer, positionContainer.get(), numBytesRead, fileWriteCompletionHandler);
    }

}
