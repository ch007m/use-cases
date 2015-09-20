package my.cool.logging;

import org.apache.felix.scr.annotations.*;
import org.ops4j.pax.logging.spi.PaxAppender;
import org.ops4j.pax.logging.spi.PaxLoggingEvent;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;

@Component(description = "My OSGI Logging Service", immediate = true)
public class LoggingService {

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
        reader.addLogListener(m_console);
    }

    @Deactivate
    public void deactivate() {
        LOGGER.log(INFO, "Logging Service stopped");
        reader.removeLogListener(m_console);
    }
}
