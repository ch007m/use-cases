package my.cool.logging;

import org.apache.felix.scr.annotations.*;
import org.ops4j.pax.logging.spi.PaxAppender;
import org.ops4j.pax.logging.spi.PaxLoggingEvent;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(description = "My Pax OSGI Logging Service", immediate = true)
@Service(PaxAppender.class)
@Properties({
        @Property(name = "org.ops4j.pax.logging.appender.name", value = "cool")
})
public class LoggingPaxService implements PaxAppender {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingPaxService.class);
    private static final Character CR = '\n';

    @Activate
    public void activate() {
        LOGGER.info("Logging Service started");
    }

    @Deactivate
    public void deactivate() {
        LOGGER.info("Logging Service stopped");
    }

    @Override
    public void doAppend(final PaxLoggingEvent log) {
        StringBuffer str = new StringBuffer();

        str.append("OSGI Cool appender ::");
        str.append(CR);
        str.append(log.getLevel() + ": '" + log.getMessage() + "'");
        str.append(", Logger Name : " + log.getLoggerName());
        str.append(", Rendered message : " + log.getRenderedMessage());
        str.append(", ThreadName : " + log.getThreadName());
        if (log.getThrowableStrRep() != null) {
            str.append(CR);
            String[] stack = log.getThrowableStrRep();
            for (String traceElement : stack) {
                str.append(CR);
                str.append("        " + traceElement);
            }
        }
        System.out.println(str.toString());
    }
}
