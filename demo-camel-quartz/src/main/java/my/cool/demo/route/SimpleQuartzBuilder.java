package my.cool.demo.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.interceptor.Tracer;

import java.io.FileNotFoundException;
import java.util.Map;

public class SimpleQuartzBuilder extends RouteBuilder {

    @Override
    public void configure() {

        Tracer tracer = new Tracer();
        tracer.getDefaultTraceFormatter().setShowBreadCrumb(false);
        tracer.getDefaultTraceFormatter().setShowNode(true);
        tracer.getDefaultTraceFormatter().setShowBody(true);
        tracer.getDefaultTraceFormatter().setShowHeaders(true);
        tracer.getDefaultTraceFormatter().setShowProperties(true);

        getContext().addInterceptStrategy(tracer);

        from("quartz://demoGroup/pollFile?cron=0/2+*+*+*+*+?&trigger.timeZone=IST")
/*          .process(new Processor() {
              @Override
              public void process(Exchange exchange) throws Exception {
                  System.out.println("Headers");
                  Map<String, Object> headers = exchange.getIn().getHeaders();
                  for (Map.Entry<String, Object> header : headers.entrySet()) {
                      System.out.print("Key : " + header.getKey() + ", ");
                      System.out.println("Value : " + header.getValue());
                  }
              }
          })*/
          .log("Cron has fired an event : ${header.scheduledFireTime}. Next is scheduled ate ${header.nextFireTime}");
    }
}