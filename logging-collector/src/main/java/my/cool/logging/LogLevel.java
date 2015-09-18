package my.cool.logging;

public enum LogLevel {
    
    INFO(3), WARNING(2), DEBUG(4), ERROR(1);

    private int value;

    private LogLevel(int value) {
        this.value = value;
    }

    public static LogLevel getLog(int value) {
        for (LogLevel l : LogLevel.values()) {
            if (l.value == value) return l;
        }
        throw new IllegalArgumentException("LogLevel not found");
    }
}
