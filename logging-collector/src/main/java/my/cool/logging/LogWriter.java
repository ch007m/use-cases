package my.cool.logging;

import org.osgi.framework.Bundle;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

public class LogWriter implements LogListener {

    // Invoked by the log service implementation for each log entry
    public void logged(LogEntry log) {

        if (log.getMessage() != null) {
            LogLevel level = LogLevel.getLog(log.getLevel());
            Bundle b = log.getBundle();
            System.out.println(level.name() + ": '" + log.getMessage() + "' - Bundle : " + b.getSymbolicName() + " (" + b.getBundleId() + ")");
            if (log.getException() != null) {
                System.out.println("Cause : " + log.getException().getMessage());
                StackTraceElement[] ste = log.getException().getStackTrace();
                for (StackTraceElement traceElement : ste) {
                    System.out.println("        " + traceElement);
                }
            }
        }

    }
}