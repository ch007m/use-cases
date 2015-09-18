package my.cool.logging;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;

@Component(description = "My Logging Service", immediate = true)
public class LoggingService {

    @Reference(referenceInterface = LogService.class)
    LogService logger;
    
    @Reference(referenceInterface = LogReaderService.class)
    LogReaderService reader;
    
/*    private static Integer INFO = Integer.parseInt(LogLevel.INFO.name());
    private static Integer DEBUG = Integer.parseInt(LogLevel.DEBUG.name());
    private static Integer WARNING = Integer.parseInt(LogLevel.WARNING.name());
    private static Integer ERROR = Integer.parseInt(LogLevel.ERROR.name());*/

    private LogWriter m_console = new LogWriter();

    @Activate
    public void start() {
        logger.log(3,"Logging Service started");
        System.out.println("Logging Service started");
        reader.addLogListener(m_console);
    }

    @Deactivate
    public void stop() {
        logger.log(3,"Logging Service stopped");
        System.out.println("Logging Service stopped");
        reader.removeLogListener(m_console);
    }
}
