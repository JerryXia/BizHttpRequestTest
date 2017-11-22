package com.github.jerryxia.devhelper.requestcapture.log;

/**
 * @author Administrator
 *
 */
/**
 * @author guqk
 *
 */
public class LogEntry {
    private long   id;
    private String httpRequestRecordId;
    private String httpRequestRecordReplayingRequestId;
    private String host;
    private String ip;
    private String loggerName;
    private String message;
    private String threadName;
    private long   timeStamp;
    private String level;

    public LogEntry(String httpRequestRecordId, String httpRequestRecordReplayingRequestId) {
        this.id = LogConstants.LOG_ENTRY_ID.incrementAndGet();
        this.httpRequestRecordId = httpRequestRecordId;
        this.httpRequestRecordReplayingRequestId = httpRequestRecordReplayingRequestId;
    }

    public long getId() {
        return id;
    }

    public String getHttpRequestRecordId() {
        return httpRequestRecordId;
    }

    public String getHttpRequestRecordReplayingRequestId() {
        return httpRequestRecordReplayingRequestId;
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
}
