package my.cool.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

public class SSLClient {

    public static void main(String[] args) {
        NetClientOptions options = new NetClientOptions()
                .setSsl(true)
                .setTrustAll(true);
        Vertx vertx = Vertx.vertx();
        NetClient client = vertx.createNetClient(options);

        client.connect(8888, "localhost", res -> {
            if (res.succeeded()) {
                System.out.println("Connected!");
                NetSocket socket = res.result();
                socket.write("Send some data");
            } else {
                System.out.println("Failed to connect: " + res.cause().getMessage());
                res.cause().printStackTrace();
            }
        });
    }
    
}
