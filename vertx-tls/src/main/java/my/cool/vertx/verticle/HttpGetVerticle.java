package my.cool.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpGetVerticle extends AbstractVerticle {

    private final static io.vertx.core.logging.Logger logger = io.vertx.core.logging.LoggerFactory
            .getLogger(HttpGetVerticle.class);
    
    private HttpServer server;
    private HttpServerOptions options;
    private Vertx vertx;
    private VertxOptions vertxOptions;

    public HttpGetVerticle() {
        options = new HttpServerOptions().setSsl(true).setKeyStoreOptions(new JksOptions()
                .setPath("/Users/chmoulli/MyProjects/use-cases/vertx-tls/src/main/resources/server.jks")
                .setPassword("dabou456"));

        vertx = Vertx.vertx();
    }
    
    @Override
    public void start() {
        server = vertx.createHttpServer(options).requestHandler(req -> {
            HttpServerResponse resp = req.response();
            MultiMap map = req.headers();
            logger.info("Header - content-length : " + map.get("content-length"));
            logger.info("Header - host : " + map.get("Host"));
            if (req.method() == HttpMethod.GET) {
                logger.info("GET request received");
                resp.setChunked(true).write("Get Method has been called").end();
            } else {
                resp.setStatusCode(400).end();
            }
        }).listen(8888);

        logger.info("HTTP Server started on port - 8888");
    }
    
    @Override
    public void stop() {
        server.close();
        logger.info("HTTP Server stopped");
    }
}