package io.vertx.example.kubernetes;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.configuration.ConfigurationService;
import io.vertx.ext.configuration.ConfigurationServiceOptions;
import io.vertx.ext.configuration.ConfigurationStoreOptions;

public class MyVerticle extends AbstractVerticle {

    @Override
    public void start() {
        ConfigurationStoreOptions store1 = new ConfigurationStoreOptions();
        store1.setType("configmap")
                .setFormat("properties")
                .setConfig(new JsonObject()
                        .put("namespace", "vertx-demo")
                        .put("name", "game-config")
                        .put("key", "game.properties"));

        ConfigurationStoreOptions store3 = new ConfigurationStoreOptions();
        store3.setType("configmap")
                .setConfig(new JsonObject()
                        .put("namespace", "vertx-demo")
                        .put("name", "game-config")
                        .put("key", "ui.properties"));

        ConfigurationStoreOptions store2 = new ConfigurationStoreOptions();
        store2.setType("configmap")
                .setConfig(new JsonObject()
                        .put("namespace", "vertx-demo")
                        .put("name", "special-config"));


        ConfigurationService conf = ConfigurationService.create(vertx, new ConfigurationServiceOptions()
                .addStore(store1).addStore(store2).addStore(store3));

        conf.getConfiguration(ar -> {
            if (ar.failed()) {
                ar.cause().printStackTrace();
            } else {
                System.out.println(ar.result().encodePrettily());
            }
        });

        conf.listen(newConfiguration -> {
            System.out.println("New configuration: " + newConfiguration.encodePrettily());
        });
    }

}