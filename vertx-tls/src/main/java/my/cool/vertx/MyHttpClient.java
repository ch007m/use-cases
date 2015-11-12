package my.cool.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.JksOptions;

public class MyHttpClient {

    private final static Logger logger = LoggerFactory.getLogger(MyHttpClient.class);

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
            logger.info("Response received : " + resp.statusCode());
            logger.info("Message : " + resp.statusMessage());
            resp.bodyHandler(buff -> {
                logger.info("Body length : " + buff.length());
                logger.info("Response : " + buff.toString());
            });
        });
        // Now do stuff with the request
        request.putHeader("content-length", "1000").putHeader("content-type", "text/plain");
        request.write("This is a message").end();
    }

}
