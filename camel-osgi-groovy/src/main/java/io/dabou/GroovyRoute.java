package io.dabou;

import org.apache.camel.builder.RouteBuilder;

public class GroovyRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("timer:groovy").routeId("groovy-token")
           .setBody().constant("This is a message containing double quotes. Example --> \"Hello\".")
           .setHeader("token")
                .groovy("body.replaceAll('\"','')");
    }
}
