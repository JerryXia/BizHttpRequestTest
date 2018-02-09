/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.support.log;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

import com.github.jerryxia.devhelper.requestcapture.log.LogEntry;
import com.github.jerryxia.devhelper.requestcapture.support.RequestCaptureConstants;

/**
 * @author Administrator
 *
 */
public class LogbackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private static final String TAB       = "\t";

    private InetAddress localHost;
    private String      hostName;
    private String      ip;

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
        RequestCaptureConstants.LOG_EXT_ENABLED = true;
    }

    @Override
    protected void append(ILoggingEvent event) {
        String httpRequestRecordId = RequestCaptureConstants.HTTP_REQUEST_RECORD_ID.get();
        LogEntry log = new LogEntry(httpRequestRecordId);
        log.setHost(this.hostName);
        log.setIp(this.ip);
        log.setLoggerName(event.getLoggerName());

        IThrowableProxy throwableProxy = event.getThrowableProxy();
        if (throwableProxy != null) {
            // 1024 * 16
            StringBuffer sb = new StringBuffer(16384);
            sb.append(event.getFormattedMessage()).append(System.lineSeparator()).append(System.lineSeparator());
            log.setMessage(formatException(throwableProxy, sb));
        } else {
            log.setMessage(event.getFormattedMessage());
        }

        log.setThreadName(event.getThreadName());
        log.setTimeStamp(event.getTimeStamp());
        log.setLevel(event.getLevel().toString());
        RequestCaptureConstants.RECORD_MANAGER.currentLogEntryManager().allocEventProducer().publish(log);
    }

    private String formatException(IThrowableProxy error, StringBuffer sb) {
        formatTopLevelError(error, sb);
        formatStackTraceElements(error, sb);
        sb.append(System.lineSeparator());

        IThrowableProxy causeError = error.getCause();
        while (causeError != null) {
            formatTopLevelError(causeError, sb);
            formatStackTraceElements(causeError, sb);
            sb.append(System.lineSeparator());
            causeError = causeError.getCause();
        }
        return sb.toString();
    }

    private void formatTopLevelError(IThrowableProxy error, StringBuffer sb) {
        sb.append(error.getClassName()).append(": ").append(error.getMessage());
    }

    private void formatStackTraceElements(IThrowableProxy error, StringBuffer sb) {
        StackTraceElementProxy[] elements = error.getStackTraceElementProxyArray();
        if (elements != null) {
            for (StackTraceElementProxy e : elements) {
                sb.append(System.lineSeparator()).append(TAB).append(e.getSTEAsString());
            }
        }
    }
}