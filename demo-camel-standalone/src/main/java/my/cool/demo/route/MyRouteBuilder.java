package my.cool.demo.route;

import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {

    public void configure() {

        //getContext().setTracing(true);

        from("file:src/data?noop=true&recursive=false&sendEmptyMessageWhenIdle=true")
            .choice()
                .when()
                  .simple("${body} == 'null'")
                  .log("Empty Message received")

            .choice()
                .when(xpath("/person/city = 'London'"))
                    .log("UK message")
                    .to("file:src/data/uk")
                .otherwise()
                    .log("Other message")
                    .to("file:src/data/others");
    }

}
