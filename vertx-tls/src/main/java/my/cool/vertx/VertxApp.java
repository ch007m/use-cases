package my.cool.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import my.cool.vertx.verticle.HttpGetVerticle;

public class VertxApp {
    
    private final static Logger logger = LoggerFactory.getLogger(VertxApp.class);

    public static void main(String[] args) throws InterruptedException {
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new HttpGetVerticle());

        logger.info("HTTP Verticle deployed");
    }
}
