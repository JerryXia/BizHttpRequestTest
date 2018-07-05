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
    private InetAddress localHost;
    private String      hostName;
    private String      ip;

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
        RequestCaptureConstants.LOG_EXT_ENABLED = true;
    }

    @Override
    public void close() {
        if (super.closed) {
            return;
        }
        super.closed = true;
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

    @Override
    protected void append(LoggingEvent event) {
        String httpRequestRecordId = RequestCaptureConstants.HTTP_REQUEST_RECORD_ID.get();
        LogEntry log = new LogEntry(httpRequestRecordId);
        log.setHost(this.hostName);
        log.setIp(this.ip);
        log.setLoggerName(event.getLoggerName());

        String[] throwableStrRep = event.getThrowableStrRep();
        if (throwableStrRep != null && throwableStrRep.length > 0) {
            // 1024 * 16
            StringBuffer sb = new StringBuffer(16384);
            sb.append(event.getRenderedMessage()).append(System.lineSeparator()).append(System.lineSeparator());
            for (String line : throwableStrRep) {
                sb.append(line).append(System.lineSeparator());
            }
            log.setMessage(sb.toString());
        } else {
            log.setMessage(event.getRenderedMessage());
        }

        log.setThreadName(event.getThreadName());
        log.setTimeStamp(event.getTimeStamp());
        log.setLevel(event.getLevel().toString());
        RequestCaptureConstants.RECORD_MANAGER.currentLogEntryManager().allocEventProducer().tryPublish(log);
    }
}