package my.cool.logging;

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

public class LogWriter implements LogListener {
    
    // Invoked by the log service implementation for each log entry
    public void logged(LogEntry entry) {
        System.out.println("Level : " + entry.getLevel() + ", " + entry.getMessage() + ", Bundle : " + entry.getBundle().getSymbolicName() + ", Cause : " + entry.getException().getCause());
        StackTraceElement[] ste = entry.getException().getStackTrace();
        for (StackTraceElement traceElement : ste) {
            System.out.println(traceElement);
        }
    }
}