package com.guqiankun.devhelper.requestcapture.support.log4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.guqiankun.devhelper.Constants;
import com.guqiankun.devhelper.requestcapture.RecordManager;
import com.guqiankun.devhelper.requestcapture.log.LogEntry;

public class Log4jMemoryAppender extends AppenderSkeleton {

    private InetAddress      localHost;
    private String           hostName;
    private String           ip;

    public Log4jMemoryAppender() {
        try {
            localHost = InetAddress.getLocalHost();
            hostName = localHost.getHostName();
            ip = localHost.getHostAddress();
        } catch (UnknownHostException e) {
            hostName = "UnKnown";
            ip = "0.0.0.0";
        }
    }

    @Override
    public void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

    @Override
    protected void append(LoggingEvent event) {
        long id = Constants.LOG_ENTRY_ID.incrementAndGet();
        String httpRequestRecordId = Constants.HTTP_REQUEST_RECORD_ID.get();
        String httpRequestRecordRequestId = Constants.HTTP_REQUEST_RECORD_REQUEST_ID.get();
        LogEntry log = new LogEntry(id, httpRequestRecordId, httpRequestRecordRequestId);
        log.setHost(this.hostName);
        log.setIp(this.ip);
        log.setLoggerName(event.getLoggerName());
        log.setMessage(event.getRenderedMessage());
        log.setThreadName(event.getThreadName());
        log.setTimeStamp(event.getTimeStamp());
        log.setLevel(event.getLevel().toString());
        RecordManager.getInstance().currentRecordLogList().insert(log);
    }
}
