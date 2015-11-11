package my.cool.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class) public class MyFirstVerticleTest {

    private Vertx vertx;

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
                .setKeyStoreOptions(
                    new JksOptions()
                       .setPath("/Users/chmoulli/MyProjects/use-cases/vertx-tls/src/main/resources/client.jks")
                       .setPassword("dabou456"));

        vertx.createNetClient(opts).connect(8888, "localhost", new Handler<AsyncResult<NetSocket>>() {
            @Override public void handle(AsyncResult<NetSocket> event) {
                async.complete();
            }
        });
    }
}