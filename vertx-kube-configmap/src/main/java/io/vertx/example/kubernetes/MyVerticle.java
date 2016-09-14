package io.vertx.example.kubernetes;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.configuration.ConfigurationService;
import io.vertx.ext.configuration.ConfigurationServiceOptions;
import io.vertx.ext.configuration.ConfigurationStoreOptions;

public class MyVerticle extends AbstractVerticle {

    @Override
    public void start() {
        ConfigurationStoreOptions gameStore = new ConfigurationStoreOptions();
        gameStore.setType("configmap")
                .setFormat("properties")
                .setConfig(new JsonObject()
                        .put("namespace", "vertx-demo")
                        .put("name", "game-config")
                        // Name of the key corresponds to the name of the file containing the key/value imported
                        .put("key", "game.properties"));

        ConfigurationStoreOptions uiStore = new ConfigurationStoreOptions();
        uiStore.setType("configmap")
                .setConfig(new JsonObject()
                        // Should work without defining the namespace as the env var KUBERNETES_NAMESPACE will be used
                        // .put("namespace", "vertx-demo")
                        .put("name", "ui-config")
                        .put("key", "ui.json"));

        ConfigurationStoreOptions appStore = new ConfigurationStoreOptions();
        appStore.setType("configmap")
                .setConfig(new JsonObject()
                        .put("namespace", "vertx-demo")
                        .put("name", "app-config"));

        ConfigurationService conf = ConfigurationService.create(vertx, new ConfigurationServiceOptions()
                .addStore(gameStore).addStore(uiStore).addStore(appStore));

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