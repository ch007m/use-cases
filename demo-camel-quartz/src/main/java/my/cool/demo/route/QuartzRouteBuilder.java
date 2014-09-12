package my.cool.demo.route;

import org.apache.camel.*;
import org.apache.camel.builder.DeadLetterChannelBuilder;
import org.apache.camel.builder.ErrorHandlerBuilderSupport;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.ErrorHandler;

public class QuartzRouteBuilder extends RouteBuilder {

    @EndpointInject(uri = "quartz://SuccessCronJob?cron=0+2-4+9+?+*+WED&trigger.timeZone=UTC")
    Endpoint SuccessCron;

    @EndpointInject(uri = "quartz://FailureCronJob?cron=0+30-33+10+?+*+WED&trigger.timezone=UTC")
    Endpoint FailureCron;

    @EndpointInject(uri = "file://source?readLock=changed&readLockMinLength=0&delay=3000&noop=true&idempotent=false")
    Endpoint source;

    @EndpointInject(uri = "file://destination")
    Endpoint destination;

    @EndpointInject(uri = "file://received")
    Endpoint received;

    @EndpointInject(uri = "file://failed?noop=true&idempotent=false")
    Endpoint failed;

    @Override
    public void configure() throws Exception {

        DeadLetterChannelBuilder DLQ = (DeadLetterChannelBuilder) deadLetterChannel("direct:deadletterQ").useOriginalMessage().retryAttemptedLogLevel(LoggingLevel.WARN);
        DLQ = (DeadLetterChannelBuilder) DLQ.maximumRedeliveries(10);
        DLQ = (DeadLetterChannelBuilder) DLQ.redeliveryDelay(3000);

        //Success Scheduler Route
        from(SuccessCron).routeId("successRoute")
                .errorHandler(DLQ)
                .pollEnrich(source.getEndpointUri())
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("File ");
                    }
                })
                .multicast().stopOnException()
                .to(destination)
                .onCompletion()
                .onCompleteOnly()
                .setHeader("subject").constant("TestMail")
                .setBody().constant("File Processed successfully")
                .to("mock:mymail").end()
                .to(received);


        //dead letter route (move failed inputs to failed location)
        from("direct:deadletterQ").routeId("deadletterRoute")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("DLQ called");
                    }
                })
                .to(failed);

        //failure scheduler route
        from(FailureCron).routeId("failedRoute")
                .errorHandler(DLQ)
                .pollEnrich(failed.getEndpointUri())
                .setHeader("subject").constant("Failed Transfer")
                .setBody().constant("File Transfer Failed")
                .to("mock:mymail");

    }
}
