package my.cool.demo.route;

import org.apache.camel.builder.DeadLetterChannelBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.interceptor.Tracer;

import java.io.FileNotFoundException;

public class DummyQuartzBuilder extends RouteBuilder {

    @Override
    public void configure() {

        Tracer tracer = new Tracer();
        tracer.getDefaultTraceFormatter().setShowBreadCrumb(false);
        tracer.getDefaultTraceFormatter().setShowNode(true);
        tracer.getDefaultTraceFormatter().setShowBody(true);
        tracer.getDefaultTraceFormatter().setShowHeaders(true);
        tracer.getDefaultTraceFormatter().setShowProperties(true);

        getContext().addInterceptStrategy(tracer);

        from("quartz://demoGroup/pollFile?cron=0/5+*+*+*+*+?&trigger.timeZone=UTC")
            .pollEnrich("file:src/data/in?move=done&moveFailed=../error&recursive=false&readLock=changed")
            .log("File has been polled - ${file:name}")
            .throwException(new FileNotFoundException());

        from("quartz://demoGroup/moveFile?cron=0/25+*+*+*+*+?&trigger.timeZone=UTC")
            .pollEnrich("file:src/data/error?recursive=false&delete=true&readLock=changed")
            .log("File has been backuped - ${file:name}")
            .to("file:src/data/backup");
    }
}