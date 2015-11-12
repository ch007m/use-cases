package my.cool.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import my.cool.vertx.verticle.MyFirstVerticle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(VertxUnitRunner.class) public class MyFirstVerticleTest {

    private Vertx vertx;
    private NetClient netClient;
    private HttpClient client;

    @Before public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(MyFirstVerticle.class.getName(), context.asyncAssertSuccess());
    }

    @After public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }


    @Test public void testMyApplication(TestContext context) {
        
        final Async async = context.async();

        NetClientOptions opts = new NetClientOptions()
                .setSsl(true)
                .setTrustAll(true)
                .setConnectTimeout(2000);

        netClient = vertx.createNetClient(opts);
        netClient.connect(8888, "localhost", res -> {
            if (res.succeeded()) {
            NetSocket socket = res.result();
            socket.write("This is a HTTP Client message");
            socket.handler(resp -> {
                System.out.println("I get a response : " + resp.toString());
                context.assertTrue(resp.toString().contains("Hello"));
            });
            async.complete();
            } else {
                System.out.println("Failed to connect: " + res.cause().getMessage());
                context.fail();
            }
        });

    }
}