package io.dabou;

import org.apache.camel.builder.RouteBuilder;

public class GroovyRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("timer:groovy?period=3000").routeId("groovy-token")
           .setBody()
              .constant("This is a message containing double quotes. Example --> \"Hello\".")
           .log(">> Before to execute this groovy script - body.replaceAll('\"','')")
           .log("${body}")
           .setBody()
              .groovy("body.replaceAll('\"','')")
           .log(">> After Groovy script executed - Double quotes have been removed")
           .log("${body}");
    }
}
