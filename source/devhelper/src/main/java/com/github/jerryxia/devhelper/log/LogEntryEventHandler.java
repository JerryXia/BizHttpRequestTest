package com.github.jerryxia.devhelper.log;

import com.github.jerryxia.devhelper.event.ValueWrapperEvent;
import com.lmax.disruptor.EventHandler;

public class LogEntryEventHandler implements EventHandler<ValueWrapperEvent> {
    private final LogEntryStorage logEntryStorage;

    public LogEntryEventHandler(LogEntryStorage logEntryStorage) {
        this.logEntryStorage = logEntryStorage;
    }

    public LogEntryStorage getLogEntryStorage() {
        return logEntryStorage;
    }

    @Override
    public void onEvent(ValueWrapperEvent event, long sequence, boolean endOfBatch) {
        LogEntry record = (LogEntry) event.getValue();
        this.logEntryStorage.save(record);
    }
}
