/**
 * 
 */
package org.anonymous.devhelper.requestcapture.support.log;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

import org.anonymous.devhelper.requestcapture.log.LogEntry;
import org.anonymous.devhelper.requestcapture.support.Constants;

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
        Constants.LOG_EXT_ENABLED_MAP.put("logback", Boolean.TRUE);
        Constants.LOG_EXT_ENABLED_STATUS = true;
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
    protected void append(ILoggingEvent event) {
        String httpRequestRecordId = Constants.HTTP_REQUEST_RECORD_ID.get();
        String httpRequestRecordReplayingRequestId = Constants.HTTP_REQUEST_RECORD_REPLAYING_REQUEST_ID.get();
        LogEntry log = new LogEntry(httpRequestRecordId, httpRequestRecordReplayingRequestId);
        log.setHost(this.hostName);
        log.setIp(this.ip);
        log.setLoggerName(event.getLoggerName());
        log.setMessage(event.getFormattedMessage());
        log.setThreadName(event.getThreadName());
        log.setTimeStamp(event.getTimeStamp());
        log.setLevel(event.getLevel().toString());
        Constants.RECORD_MANAGER.currentLogEntryManager().allocEventProducer().publish(log);
    }

}