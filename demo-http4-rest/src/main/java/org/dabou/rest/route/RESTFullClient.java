package org.dabou.rest.route;

import org.dabou.rest.model.TankLevelReport;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.util.HashMap;

public class RESTFullClient extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        onException(HttpOperationFailedException.class)
                .handled(false)
                .log(">> Something wrong happened");

        from("direct:wayne-get-token").setExchangePattern(ExchangePattern.InOut)
           .doTry()
             .to("https4://wayne-token-service")
                //.choice()
                //.when().simple("${header.CamelHttpResponseCode} == '200'")
                .convertBodyTo(String.class)
                .setHeader("wayne-token").groovy("body.replaceAll('\"','')")
                .log(">> Wayne Token : ${header.wayne-token}")
                //.endChoice()
           .doCatch(Exception.class)
                .log(">> EXCEPTION, EXCEPTION !!!!!!!!!")
           .end();

        from("direct:wayne-report").setExchangePattern(ExchangePattern.InOut)
            .onException(HttpOperationFailedException.class)
                .handled(false)
                .log(">> Something wrong happened")
                .end()

            .to("https4://wayne-report")
            .choice()
                .when().simple("${header.CamelHttpResponseCode} == '200'")
                    .convertBodyTo(String.class)
                    .unmarshal().json(JsonLibrary.Jackson, TankLevelReport.class)
                    //.log(">> Report : ${body}")
        ;

    }
}
