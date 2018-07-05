/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.log;

import com.lmax.disruptor.EventTranslatorOneArg;

/**
 * @author Administrator
 *
 */
public class LogEntryEventTranslator implements EventTranslatorOneArg<LogEntryEvent, LogEntry> {

    @Override
    public void translateTo(LogEntryEvent event, long sequence, LogEntry arg0) {
        event.setValue(arg0);
    }
}
