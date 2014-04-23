package net.xylophones.testing;

import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class TemporaryClasspathFile extends ExternalResource {

    private final TemporaryFolder temporaryFolder;

    public TemporaryClasspathFile() {
        temporaryFolder = new TemporaryFolder();
    }

    @Override
    protected void before() throws Throwable {
        temporaryFolder.create();
    }

    @Override
    protected void after() {
        temporaryFolder.delete();
    }

    public File createFileFromClasspath(String resourcePath) throws IOException {
        checkResourcePath(resourcePath);

        String tempFilename = getFilenameFromClasspathResource(resourcePath);
        File file = temporaryFolder.newFile(tempFilename);

        copyClasspathResourceToFile(resourcePath, file);

        return file;
    }

    private void checkResourcePath(String resourcePath) {
        if (resourcePath == null) {
            throw new NullPointerException("resource path was null");
        } else if (resourcePath.length() == 0) {
            throw new IllegalArgumentException("resource path was empty");
        }
    }

    private String getFilenameFromClasspathResource(String resourceName) {
        int beginIndex = resourceName.lastIndexOf('/');

        if (beginIndex >= 0) {
            int beginAfterSlash = beginIndex + 1;
            return resourceName.substring(beginAfterSlash);
        } else {
            return resourceName;
        }
    }

    private void copyClasspathResourceToFile(String resourcePath, File file) throws IOException {
        try (
            InputStream inputStream = TemporaryClasspathFile.class.getResourceAsStream(resourcePath);
        ) {
            if (inputStream == null) {
                throw new IOException("Classpath resource '" + resourcePath + "' not found");
            }
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

}
