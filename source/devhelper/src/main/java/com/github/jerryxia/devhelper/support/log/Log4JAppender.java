/**
 * 
 */
package com.github.jerryxia.devhelper.support.log;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.github.jerryxia.devhelper.Constants;
import com.github.jerryxia.devhelper.log.LogEntry;
import com.github.jerryxia.devhelper.log.LogEntrySource;

/**
 * @author Administrator
 *
 */
public class Log4JAppender extends AppenderSkeleton {
    private InetAddress localHost;
    private String      hostName;
    private String      ip;

    /**
     * Constructor.
     */
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
        Constants.LOG_EXT_ENABLED_MAP.put(LogProvider.log4j.name(), Boolean.TRUE);
        Constants.LOG_EXT_ENABLED = true;
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
        LogEntrySource logEntrySouce = LogConstants.LOG_ENTRY_SOURCE.get();
        String recordId = LogConstants.RECORD_ID.get();
        LogEntry log = new LogEntry(logEntrySouce, recordId);
        log.setHost(this.hostName);
        log.setIp(this.ip);
        log.setLoggerName(event.getLoggerName());

        String[] throwableStrRep = event.getThrowableStrRep();
        if (throwableStrRep != null && throwableStrRep.length > 0) {
            // 1024 * 16
            StringBuilder sb = new StringBuilder(16384);
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
        Constants.EVENT_WORKING_GROUP.allocEventProducer().tryPublish(log);
    }
}