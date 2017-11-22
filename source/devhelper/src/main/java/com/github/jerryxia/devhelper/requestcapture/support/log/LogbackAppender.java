/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.support.log;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

import com.github.jerryxia.devhelper.requestcapture.log.LogEntry;
import com.github.jerryxia.devhelper.requestcapture.support.RequestCaptureConstants;

/**
 * @author Administrator
 *
 */
public class LogbackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private InetAddress      localHost;
    private String           hostName;
    private String           ip;

    /**
     * Constructeur.
     */
    public LogbackAppender() {
        super();
        try {
            localHost = InetAddress.getLocalHost();
            hostName = localHost.getHostName();
            ip = localHost.getHostAddress();
        } catch (UnknownHostException e) {
            hostName = "UnKnown";
            ip = "0.0.0.0";
        }
        RequestCaptureConstants.LOG_EXT_ENABLED_MAP.put("logback", Boolean.TRUE);
        RequestCaptureConstants.LOG_EXT_ENABLED_STATUS = true;
    }

    @Override
    protected void append(ILoggingEvent event) {
        String httpRequestRecordId = RequestCaptureConstants.HTTP_REQUEST_RECORD_ID.get();
        LogEntry log = new LogEntry(httpRequestRecordId);
        log.setHost(this.hostName);
        log.setIp(this.ip);
        log.setLoggerName(event.getLoggerName());
        log.setMessage(event.getFormattedMessage());
        log.setThreadName(event.getThreadName());
        log.setTimeStamp(event.getTimeStamp());
        log.setLevel(event.getLevel().toString());
        RequestCaptureConstants.RECORD_MANAGER.currentLogEntryManager().allocEventProducer().publish(log);
    }

}