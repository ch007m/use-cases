package my.cool.vertx;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;

public class HttpsServer {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
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
                socket.write("some data");
            });

            socket.closeHandler(v -> {
                System.out.println("The socket has been closed");
            });
        });
        server.listen(8888);
    }
}

