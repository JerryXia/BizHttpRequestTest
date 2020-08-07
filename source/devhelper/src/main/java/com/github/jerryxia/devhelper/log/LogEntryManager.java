package com.github.jerryxia.devhelper.log;

import com.github.jerryxia.devhelper.event.ValueWrapperEventProducer;
import com.github.jerryxia.devhelper.event.ValueWrapperEventStat;
import com.github.jerryxia.devhelper.event.WorkingGroup;

/**
 * @author Administrator
 *
 */
public class LogEntryManager {
    private final LogEntryStorage      logEntryStorage;
    private final LogEntryEventHandler consumer;
    private WorkingGroup               eventWorkingGroup;

    public LogEntryManager() {
        // init consumer
        this.logEntryStorage = new LogEntryMemoryStorage();
        this.consumer = new LogEntryEventHandler(this.logEntryStorage);
    }

    public ValueWrapperEventProducer allocEventProducer() {
        return this.eventWorkingGroup.allocEventProducer();
    }

    public ValueWrapperEventStat viewLogEntryEventStat() {
        return this.eventWorkingGroup.viewEventStats().allocate(LogEntry.class);
    }

    public LogEntryStorage currentLogEntryStorage() {
        return this.logEntryStorage;
    }

    public LogEntryEventHandler currentConsumer() {
        return this.consumer;
    }

    public void setEventWorkingGroup(WorkingGroup eventWorkingGroup) {
        this.eventWorkingGroup = eventWorkingGroup;
    }

    public WorkingGroup getEventWorkingGroup() {
        return eventWorkingGroup;
    }

}
