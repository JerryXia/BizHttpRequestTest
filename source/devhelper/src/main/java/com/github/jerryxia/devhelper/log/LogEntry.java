package com.github.jerryxia.devhelper.log;

/**
 * @author guqk
 *
 */
public class LogEntry {
    private long           id;
    private LogEntrySource source;
    private String         recordId;
    private String         host;
    private String         ip;
    private String         loggerName;
    private String         message;
    private String         threadName;
    private long           timeStamp;
    private String         level;

    public LogEntry(LogEntrySource source, String recordId) {
        this.id = LogConstants.LOG_ENTRY_ID.incrementAndGet();
        this.source = source;
        this.recordId = recordId;
    }

    public long getId() {
        return id;
    }

    public LogEntrySource getSource() {
        return source;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getHttpRequestRecordId() {
        return LogEntrySource.HTTP_REQUEST == this.source ? this.recordId : null;
    }

    public String getTaskScheduleRecordId() {
        return LogEntrySource.TASK_SCHEDULE == this.source ? this.recordId : null;
    }
}
