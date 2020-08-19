package com.github.jerryxia.devhelper.task;

import com.github.jerryxia.devhelper.event.ValueWrapperEventProducer;
import com.github.jerryxia.devhelper.event.ValueWrapperEventStat;
import com.github.jerryxia.devhelper.event.WorkingGroup;

/**
 * @author guqiankun
 *
 */
public class TaskRunRecordManager {
    private final TaskRunRecordStorage      recordStorage;
    private final TaskRunRecordEventHandler consumer;
    private WorkingGroup                    eventWorkingGroup;

    public TaskRunRecordManager() {
        // init consumer
        this.recordStorage = new TaskRunRecordMemoryStorage();
        this.consumer = new TaskRunRecordEventHandler(this.recordStorage);
    }

    public ValueWrapperEventProducer allocEventProducer() {
        return this.eventWorkingGroup.allocEventProducer();
    }

    public ValueWrapperEventStat viewTaskRunRecordEventStat() {
        return this.eventWorkingGroup.viewEventStats().allocate(TaskRunRecord.class);
    }

    public TaskRunRecordStorage currentHttpRequestRecordStorage() {
        return this.recordStorage;
    }

    public TaskRunRecordEventHandler currentConsumer() {
        return this.consumer;
    }

    public WorkingGroup getEventWorkingGroup() {
        return eventWorkingGroup;
    }

    public void setEventWorkingGroup(WorkingGroup eventWorkingGroup) {
        this.eventWorkingGroup = eventWorkingGroup;
    }

}
