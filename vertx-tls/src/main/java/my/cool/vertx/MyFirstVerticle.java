package my.cool.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;

public class MyFirstVerticle extends AbstractVerticle {

    @Override
    public void start() {
        NetServerOptions options = new NetServerOptions()
                .setSsl(true)
                .setKeyStoreOptions(
                        new JksOptions().setPath(
                                "/Users/chmoulli/MyProjects/use-cases/vertx-tls/src/main/resources/keystore.jks")
                                .setPassword("dabou456"));
        
        NetServer server = vertx.createNetServer(options);
        server.connectHandler(socket -> {
            socket.handler(buffer -> {
                System.out.println("I received some bytes: " + buffer.length());
                // Write a string in UTF-8 encoding
                socket.write("<h1>Hello from my first " + "Vert.x 3 application</h1>");
            });

            socket.closeHandler(v -> {
                System.out.println("The socket has been closed");
            });
        });
        server.listen(8888);
    }
}