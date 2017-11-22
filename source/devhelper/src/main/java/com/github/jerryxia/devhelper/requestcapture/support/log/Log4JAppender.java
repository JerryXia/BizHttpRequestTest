/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.support.log;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.github.jerryxia.devhelper.requestcapture.log.LogEntry;
import com.github.jerryxia.devhelper.requestcapture.support.RequestCaptureConstants;

/**
 * @author Administrator
 *
 */
public class Log4JAppender extends AppenderSkeleton {

    private InetAddress      localHost;
    private String           hostName;
    private String           ip;

    public Log4JAppender() {
        super();
        try {
            localHost = InetAddress.getLocalHost();
            hostName = localHost.getHostName();
            ip = localHost.getHostAddress();
        } catch (UnknownHostException e) {
            hostName = "UnKnown";
            ip = "0.0.0.0";
        }
        RequestCaptureConstants.LOG_EXT_ENABLED_MAP.put("log4j", Boolean.TRUE);
        RequestCaptureConstants.LOG_EXT_ENABLED_STATUS = true;
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
//        final Throwable throwable;
//        if (event.getThrowableInformation() == null) {
//            throwable = null;
//        } else {
//            throwable = event.getThrowableInformation().getThrowable();
//        }
        String httpRequestRecordId = RequestCaptureConstants.HTTP_REQUEST_RECORD_ID.get();
        LogEntry log = new LogEntry(httpRequestRecordId);
        log.setHost(this.hostName);
        log.setIp(this.ip);
        log.setLoggerName(event.getLoggerName());
        log.setMessage(event.getRenderedMessage());
        log.setThreadName(event.getThreadName());
        log.setTimeStamp(event.getTimeStamp());
        log.setLevel(event.getLevel().toString());
        RequestCaptureConstants.RECORD_MANAGER.currentLogEntryManager().allocEventProducer().publish(log);
    }
}