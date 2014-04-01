package net.xylophones.nio.examples;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelReadExample {

    public static void main(String[] args) throws IOException {
        String filePath = "D:\\Users\\Will\\Projects\\general\\nio\\src\\main\\resources\\sample_data.txt";
        RandomAccessFile file = new RandomAccessFile(filePath , "rw");
        FileChannel channel = file.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(128);

        int numBytesRead = channel.read(buffer);
        while (numBytesRead != -1) {
            buffer.flip();

            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }

            buffer.clear();
            numBytesRead = channel.read(buffer);
        }

    }

}
