package net.xylophones.testing;

import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class TemporaryClasspathFileUsageTest {

    @Rule
    public TemporaryClasspathFile claspathFile = new TemporaryClasspathFile();

    @Test
    public void checkFileCreated() throws IOException {
        File file = claspathFile.createFileFromClasspath("/files/textfile.txt");
        assertTrue(file.exists());
    }
}
