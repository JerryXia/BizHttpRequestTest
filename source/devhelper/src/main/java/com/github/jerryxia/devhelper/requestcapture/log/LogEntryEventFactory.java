/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.log;

import com.lmax.disruptor.EventFactory;

/**
 * @author Administrator
 *
 */
public class LogEntryEventFactory implements EventFactory<LogEntryEvent> {

    @Override
    public LogEntryEvent newInstance() {
        return new LogEntryEvent();
    }
}