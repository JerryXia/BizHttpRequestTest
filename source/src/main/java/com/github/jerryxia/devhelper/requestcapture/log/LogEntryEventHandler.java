/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.log;

import com.lmax.disruptor.EventHandler;

import com.github.jerryxia.devhelper.util.Assert;

/**
 * @author Administrator
 *
 */
public class LogEntryEventHandler implements EventHandler<LogEntryEvent> {

    private final LogEntryStorage   logEntryStorage;
    private final LogEntryEventStat eventStat;

    public LogEntryEventHandler(LogEntryStorage logEntryStorage, LogEntryEventStat logEntryEventStat) {
        Assert.notNull(logEntryStorage);
        Assert.notNull(logEntryEventStat);
        this.logEntryStorage = logEntryStorage;
        this.eventStat = logEntryEventStat;
    }

    @Override
    public void onEvent(LogEntryEvent event, long sequence, boolean endOfBatch) {
        boolean saveSuccessed = this.logEntryStorage.save(event.getValue());
        if (saveSuccessed) {
            this.eventStat.incrementConsumerSuccessCount();
        } else {
            this.eventStat.incrementConsumerFailCount();
        }
    }
}