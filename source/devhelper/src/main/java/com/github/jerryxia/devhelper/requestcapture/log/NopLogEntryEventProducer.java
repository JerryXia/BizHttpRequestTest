/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.log;

/**
 * @author Administrator
 *
 */
public class NopLogEntryEventProducer implements LogEntryEventProducer {

    @Override
    public void publish(LogEntry record) {
        // ignore
    }

    @Override
    public void tryPublish(LogEntry record) {
        // ignore
    }

}
