package my.cool.demo;

import org.apache.http.*;
import org.apache.http.auth.*;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException, AuthenticationException {

        Logger LOG = LoggerFactory.getLogger(App.class);

        String GERRIT_URL = "http://localhost:8080/a/projects/demo46";

        DefaultHttpClient httpclient = new DefaultHttpClient();
        DefaultHttpClient httpclientPost = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(GERRIT_URL);
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

                HttpPost httpPost = new HttpPost(GERRIT_URL);
                httpPost.addHeader(digestScheme.authenticate(creds, httpPost));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String responseBody = httpclientPost.execute(httpPost, responseHandler);
                System.out.println("responseBody : " + responseBody);
            }

        } catch (MalformedChallengeException e) {
            e.printStackTrace();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (HttpResponseException e) {
            System.out.println("Response from Gerrit Server : " + e.getMessage());
        } finally {
            httpclient.getConnectionManager().shutdown();
            httpclientPost.getConnectionManager().shutdown();
        }
    }
}
