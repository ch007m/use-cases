package my.cool.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.net.JksOptions;

public class MyHttpClient {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        HttpClientOptions options = new HttpClientOptions()
                .setSsl(true)
                .setTrustStoreOptions(new JksOptions()
                    .setPassword("dabou456")
                    .setPath("/Users/chmoulli/MyProjects/use-cases/vertx-tls/src/main/resources/client.jks"))
                .setTrustAll(false)
                .setDefaultHost("localhost")
                .setDefaultPort(8888);

        HttpClient client = vertx.createHttpClient(options);
        HttpClientRequest request = client.get("/", resp -> {
            System.out.println("Response received : " + resp.statusCode());
            System.out.println("Message : " + resp.statusMessage());
            resp.bodyHandler(buff -> {
                System.out.println("Body length : " + buff.length());
                System.out.println("Response : " + buff.toString());
            });
        });
        // Now do stuff with the request
        request.putHeader("content-length", "1000");
        request.putHeader("content-type", "text/plain");
        request.write("This is a message");

        // Make sure the request is ended when you're done with it
        request.end();
    }

}
