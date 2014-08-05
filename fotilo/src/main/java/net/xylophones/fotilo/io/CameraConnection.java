package net.xylophones.fotilo.io;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.apache.http.client.utils.HttpClientUtils.closeQuietly;

/**
 * TODO - use response handling instead
 *
 * http://hc.apache.org/httpcomponents-client-4.4.x/httpclient/examples/org/apache/http/examples/client/ClientWithResponseHandler.java
 */
public class CameraConnection {


    private final CloseableHttpClient httpclient;
    private final String host;
    private final int port;
    private final String user;
    private final String pass;

    public CameraConnection(String host, int port, String user, String pass) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(host, port), new UsernamePasswordCredentials(user, pass));

        httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
    }

    public void streamImage(OutputStream outputStream) throws IOException {
        HttpGet httpget = new HttpGet("http://192.168.1.6:81/videostream.cgi?loginuse=admin&loginpas=admin123");

        System.out.println("Executing request " + httpget.getRequestLine());
        CloseableHttpResponse response = httpclient.execute(httpget);
        try {
            IOUtils.copy(response.getEntity().getContent(), outputStream);
        } finally {
            closeQuietly(httpclient);
        }
    }

    public void y() throws IOException {
        HttpGet httpget = new HttpGet("http://192.168.1.6:81/snapshot.cgi?user=admin&pwd=admin123");

        System.out.println("Executing request " + httpget.getRequestLine());
        CloseableHttpResponse response = httpclient.execute(httpget);
        FileOutputStream fos = new FileOutputStream("/tmp/out.jpg");
        IOUtils.copy(response.getEntity().getContent(), fos);

        try {
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
        } finally {
            response.close();
        }
    }

    public void destroy() {
        closeQuietly(httpclient);
    }
}
