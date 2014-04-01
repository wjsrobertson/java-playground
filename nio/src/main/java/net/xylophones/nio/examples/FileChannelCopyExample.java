package net.xylophones.nio.examples;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class FileChannelCopyExample {

    public static void main(String[] args) throws IOException {
        String readFilePath = "D:\\Users\\Will\\Projects\\general\\nio\\src\\main\\resources\\sample_data.txt";
        RandomAccessFile readFile = new RandomAccessFile(readFilePath , "r");
        FileChannel readFileChannel = readFile.getChannel();

        String writeFilePath = "D:\\Users\\Will\\Projects\\general\\nio\\src\\main\\resources\\sample_data_copy.txt";
        RandomAccessFile writeFile = new RandomAccessFile(writeFilePath , "rw");
        FileChannel writeFileChannel = writeFile.getChannel();

        long fileSize = readFileChannel.size();
        readFileChannel.transferTo(0, fileSize, writeFileChannel);
    }
}
