package net.xylophones.megaproxy.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.httpclient.ChunkedInputStream;
import org.apache.commons.httpclient.ChunkedOutputStream;
import org.apache.commons.io.IOUtils;

public class IOUtil {
	
	private static final int INPUT_BUFFER_SIZE = 1024;

	/**
	 * Copy {@code length} bytes from {@code input} to {@code output}
	 * 
	 * @param input
	 * @param output
	 * @param length
	 * @throws IOException
	 */
	public static int copy(InputStream input, OutputStream output, int length) throws IOException {
		final byte[] buffer = new byte[INPUT_BUFFER_SIZE];
	    long bytesRemaining = length;
	    int maxBytesToRead = Math.min(length, INPUT_BUFFER_SIZE);
	    
	    int bytesRead;
		while ((bytesRead = input.read(buffer, 0, maxBytesToRead)) != -1) {
			output.write(buffer, 0, bytesRead);
			
			bytesRemaining = bytesRemaining - bytesRead;
			
			if (bytesRemaining > 0) {
				maxBytesToRead = Math.min(length, INPUT_BUFFER_SIZE);
			} else {
				break;
			}
		}
		
		return bytesRead;
	}
	
	public static int copyChunked(InputStream input, OutputStream output) throws IOException {
		ChunkedInputStream chunkedInputStream = new ChunkedInputStream(input);
		ChunkedOutputStream chunkedOutputStream = new ChunkedOutputStream(output);
		
		int size = IOUtils.copy(chunkedInputStream, chunkedOutputStream);
		
		chunkedInputStream.close();
		chunkedOutputStream.finish();
		chunkedOutputStream.close();
		chunkedOutputStream.flush();
		
		return size;
	}
	
	public static int copyChunkedToNonChunked(InputStream input, OutputStream output) throws IOException {
		ChunkedInputStream chunkedInputStream = new ChunkedInputStream(input);

		int size = IOUtils.copy(chunkedInputStream, output);
		
		chunkedInputStream.close();
		output.flush();
		
		return size;
	}
}
