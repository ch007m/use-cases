package my.cool.logging;

import org.osgi.framework.Bundle;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

public class LogWriter implements LogListener {

    // Invoked by the log service implementation for each log entry
    public void logged(LogEntry log) {
        
        StringBuffer str = new StringBuffer();

        if (log.getMessage() != null) {
            Bundle b = log.getBundle();
            str.append(LogLevel.getLogName(log.getLevel()) + ": '" + log.getMessage() + "' - Bundle : " + b.getSymbolicName() + " (" + b.getBundleId() + ")");
            if (log.getException() != null) {
                str.append("\n");
                str.append("Cause : " + log.getException().getMessage());
                StackTraceElement[] ste = log.getException().getStackTrace();
                for (StackTraceElement traceElement : ste) {
                    str.append("\n");
                    str.append("        " + traceElement);
                }
            }

            System.out.println(str.toString());
        }

    }
}