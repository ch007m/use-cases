package my.cool.vertx;

import com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.streams.Pump;

import java.util.List;
import java.util.Map;

public class MyHttpServer {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServerOptions options = new HttpServerOptions().setSsl(true).setKeyStoreOptions(new JksOptions()
                        .setPath(
                                "/Users/chmoulli/MyProjects/use-cases/vertx-tls/src/main/resources/server.jks")
                        .setPassword("dabou456"));

        vertx.createHttpServer(options).requestHandler(req -> {
            HttpServerResponse resp = req.response();
            MultiMap map = req.headers();
            System.out.println("Header - content-length : " + map.get("content-length"));
            System.out.println("Header - host : " + map.get("Host"));
            if (req.method() == HttpMethod.GET) {
                System.out.println("GET request received");
                resp.setChunked(true);
                resp.write("Get Method has been called").end();
            } else {
                resp.setStatusCode(400).end();
            }
        }).listen(8888);
    }
}

