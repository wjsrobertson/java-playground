package net.xylophones.testing;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TemporaryClasspathFileTest {

    private TemporaryClasspathFile underTest;

    @Before
    public void setUp() throws Throwable {
        underTest = new TemporaryClasspathFile();
        underTest.before();
    }

    @Test(expected = NullPointerException.class)
    public void checkNullPointerThrownWhenResourceNameIsNull() throws IOException {
        underTest.createFileFromClasspath(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIllegalArgumentThrownWhenResourceNameIsEmptyString() throws IOException {
        underTest.createFileFromClasspath("");
    }

    @Test(expected = IOException.class)
    public void checkIOExceptionWhenClasspathResourceDoesNotExist() throws IOException {
        underTest.createFileFromClasspath("/fake/file/path.txt");
    }

    @Test
    public void checkFileCreatedWithCorrectNameWhenClasspathResourceExists() throws IOException {
        File fileFromClasspath = underTest.createFileFromClasspath("/files/textfile.txt");
        assertTrue(fileFromClasspath.exists());
    }

    @Test
    public void checkFileContentsCorrectWhenClasspathResourceExists() throws IOException {
        File fileFromClasspath = underTest.createFileFromClasspath("/files/textfile.txt");
        String fileContents = Files.readAllLines(fileFromClasspath.toPath()).get(0);
        assertEquals("test string in a test file", fileContents);
    }
}
