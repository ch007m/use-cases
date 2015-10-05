package my.cool.demo;

import org.apache.http.*;
import org.apache.http.auth.*;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App {

    public static void main(String[] args) throws IOException, AuthenticationException {

        Logger LOG = LoggerFactory.getLogger(App.class);

        /*String GERRIT_URL = "http://localhost:8080/a/projects/demo22";
        HttpClient httpclient = new DefaultHttpClient();

        HttpPost post = new HttpPost(GERRIT_URL);

        StringEntity entity = new StringEntity("string");
        entity.setContentType("application/json");
        post.setEntity(entity);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("username", "admin"));
        nvps.add(new BasicNameValuePair("password", "secret"));
        post.setEntity(new UrlEncodedFormEntity(nvps));

        DigestScheme digestAuth = new DigestScheme();
        digestAuth.overrideParamter("algorithm", "MD5");
        digestAuth.overrideParamter("uri", "/a/projects/dabou34");

        digestAuth.overrideParamter("realm", "Gerrit Code Review");
        digestAuth.overrideParamter("nonce", Long.toString(new Random().nextLong(), 36));
        digestAuth.overrideParamter("qop", "auth");
        digestAuth.overrideParamter("nc", "0");
        digestAuth.overrideParamter("cnonce", DigestScheme.createCnonce());
        digestAuth.overrideParamter("opaque","ba897c2f0f3de9c6f52d");

        try {
            digestAuth.processChallenge(null);
            //force  qop in use challenge  on return header ????!!!!
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }

        Header auth = digestAuth.authenticate(new
                UsernamePasswordCredentials("admin", "secret"), post);
        post.setHeader(auth);

        HttpResponse response = httpclient.execute(post);

        String content = EntityUtils.toString(response.getEntity());
        String status = Integer.toString(response.getStatusLine().getStatusCode());

        LOG.info("\nHTTP Status " + status + "\nMessage : " + content);
*/
/*        HttpHost targetHost = new HttpHost("localhost", 8080, "http");
        DefaultHttpClient httpclient = new DefaultHttpClient();

        final String userName = "admin";
        final String password = "secret";

        httpclient.getCredentialsProvider().setCredentials(
                new AuthScope("localhost", 8080),
                new UsernamePasswordCredentials(userName, password));

        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate DIGEST scheme object, initialize it and add it to the local
        // auth cache
        DigestScheme digestAuth = new DigestScheme();
        // Suppose we already know the realm name
        digestAuth.overrideParamter("realm", "Gerrit Code Review");
        // Suppose we already know the expected nonce value
        digestAuth.overrideParamter("nonce", Long.toString(new Random().nextLong(), 36));
        authCache.put(targetHost, digestAuth);

        // Add AuthCache to the execution context
        BasicHttpContext localcontext = new BasicHttpContext();
        localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);

        HttpGet httpget = new HttpGet("http://localhost:8080/a/projects/demo");

        try {
            HttpResponse response = httpclient.execute(targetHost, httpget, localcontext);
            HttpEntity entity = response.getEntity();

            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            if (entity != null) {
                System.out.println("Response content length: " + entity.getContentLength());
            }
            EntityUtils.consume(entity);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }*/

        DefaultHttpClient httpclient = new DefaultHttpClient();
        DefaultHttpClient httpclient2 = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://localhost:8080/a/projects/demo");
        System.out.println("Requesting : " + httpget.getURI());

        try {
            //Initial request without credentials returns "HTTP/1.1 401 Unauthorized"
            HttpResponse response = httpclient.execute(httpget);
            System.out.println(response.getStatusLine());

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {

                //Get current current "WWW-Authenticate" header from response
                // WWW-Authenticate:Digest realm="My Test Realm", qop="auth",
                //nonce="cdcf6cbe6ee17ae0790ed399935997e8", opaque="ae40d7c8ca6a35af15460d352be5e71c"
                Header authHeader = response.getFirstHeader(AUTH.WWW_AUTH);
                System.out.println("authHeader = " + authHeader);

                DigestScheme digestScheme = new DigestScheme();

                //Parse realm, nonce sent by server.
                digestScheme.processChallenge(authHeader);

                UsernamePasswordCredentials creds = new UsernamePasswordCredentials("admin", "secret");
                httpget.addHeader(digestScheme.authenticate(creds, httpget));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String responseBody = httpclient2.execute(httpget, responseHandler);
                System.out.println("responseBody : " + responseBody);
            }

        } catch (MalformedChallengeException e) {
            e.printStackTrace();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } finally {
            httpclient.getConnectionManager().shutdown();
            httpclient2.getConnectionManager().shutdown();
        }


    }
}
