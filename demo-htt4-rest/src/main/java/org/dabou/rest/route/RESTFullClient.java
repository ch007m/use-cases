package org.dabou.rest.route;

import org.dabou.rest.model.TankLevelReport;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;

public class RESTFullClient extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:wayne-get-token").setExchangePattern(ExchangePattern.InOut)
           .onException(HttpOperationFailedException.class)
                .handled(false)
                .log(">> Something wrong happened")
                .end()

           .to("https4://wayne-token-service")
           .choice()
              .when().simple("${header.CamelHttpResponseCode} == '200'")
                  .convertBodyTo(String.class)
                  .setHeader("wayne-token").groovy("body.replaceAll('\"','')")
                  .log(">> Wayne Token : ${header.wayne-token}")
        ;

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
