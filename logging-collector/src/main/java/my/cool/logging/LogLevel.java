package my.cool.logging;

public enum LogLevel {
    
    INFO(3), WARNING(2), DEBUG(4), ERROR(1);

    private int value;
    private LogLevel(int value) {
        this.value = value;
    }

    public static LogLevel getLogName(int value) {
        for (LogLevel l : LogLevel.values()) {
            if (l.value == value) return l;
        }
        throw new IllegalArgumentException("LogLevel not found");
    }

    public static int getLogLevel(LogLevel logLevel) {
        for (LogLevel l : LogLevel.values()) {
            if (l.name() == logLevel.name()) return l.value;
        }
        throw new IllegalArgumentException("LogLevel not found");
    }
    
    
}
