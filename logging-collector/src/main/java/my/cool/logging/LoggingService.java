package my.cool.logging;

import org.apache.felix.scr.annotations.*;
import org.ops4j.pax.logging.spi.PaxAppender;
import org.ops4j.pax.logging.spi.PaxLoggingEvent;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;

@Component(description = "My Logging Service", immediate = true)
@Service(PaxAppender.class)
@Properties({
        @Property(name = "org.ops4j.pax.logging.appender.name", value = "cool")
})
public class LoggingService implements PaxAppender {

    @Reference(referenceInterface = LogService.class)
    LogService LOGGER;

    @Reference(referenceInterface = LogReaderService.class)
    LogReaderService reader;

    private static Integer INFO = LogLevel.getLogLevel(LogLevel.INFO);
    private static Integer DEBUG = LogLevel.getLogLevel(LogLevel.DEBUG);
    private static Integer WARNING = LogLevel.getLogLevel(LogLevel.WARNING);
    private static Integer ERROR = LogLevel.getLogLevel(LogLevel.ERROR);

    private LogWriter m_console = new LogWriter();

    @Activate
    public void activate() {
        LOGGER.log(INFO, "Logging Service started");
        //reader.addLogListener(m_console);
    }

    @Deactivate
    public void deactivate() {
        LOGGER.log(INFO, "Logging Service stopped");
        //reader.removeLogListener(m_console);
    }

    @Override
    public void doAppend(final PaxLoggingEvent log) {
        StringBuffer str = new StringBuffer();

        str.append("APPENDER :: ");
        str.append(LogLevel.getLogName(3) + ": '" + log.getMessage() + "')");
        str.append(" - Local info : ");
        str.append(log.getLocationInformation());
        if (log.getThrowableStrRep() != null) {
            str.append("\n");
            String[] stack = log.getThrowableStrRep();
            for (String traceElement : stack) {
                str.append("\n");
                str.append("        " + traceElement);
            }
        }
        System.out.println(str.toString());
    }
}
