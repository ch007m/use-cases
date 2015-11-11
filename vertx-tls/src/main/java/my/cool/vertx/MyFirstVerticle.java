package my.cool.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;

public class MyFirstVerticle extends AbstractVerticle {

    HttpServerOptions sslOptions = new HttpServerOptions()
            .setHost("localhost")
            .setSsl(true)
            .setKeyStoreOptions(new JksOptions().setPath(
                    "/Users/chmoulli/MyProjects/use-cases/vertx-tls/src/main/resources/keystore.jks")
                    .setPassword("dabou456"));

    @Override
    public void start() {
        vertx.createHttpServer(sslOptions).requestHandler(r -> {
            r.response().end("<h1>Hello from my first " + "Vert.x 3 application</h1>");
        }).listen(8888);
    }
}