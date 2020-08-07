package com.github.jerryxia.devhelper.requestcapture;

import com.github.jerryxia.devhelper.event.ValueWrapperEventProducer;
import com.github.jerryxia.devhelper.event.ValueWrapperEventStat;
import com.github.jerryxia.devhelper.event.WorkingGroup;

/**
 * @author guqiankun
 *
 */
public class HttpRequestRecordManager {
    private final HttpRequestRecordStorage      recordStorage;
    private final HttpRequestRecordEventHandler consumer;
    private WorkingGroup                        eventWorkingGroup;

    public HttpRequestRecordManager() {
        // init consumer
        this.recordStorage = new HttpRequestRecordMemoryStorage();
        this.consumer = new HttpRequestRecordEventHandler(this.recordStorage);
    }

    public ValueWrapperEventProducer allocEventProducer() {
        return this.eventWorkingGroup.allocEventProducer();
    }

    public ValueWrapperEventStat viewHttpRequestRecordEventStat() {
        return this.eventWorkingGroup.viewEventStats().allocate(HttpRequestRecord.class);
    }

    public HttpRequestRecordStorage currentHttpRequestRecordStorage() {
        return this.recordStorage;
    }

    public HttpRequestRecordEventHandler currentConsumer() {
        return this.consumer;
    }

    public WorkingGroup getEventWorkingGroup() {
        return eventWorkingGroup;
    }

    public void setEventWorkingGroup(WorkingGroup eventWorkingGroup) {
        this.eventWorkingGroup = eventWorkingGroup;
    }

}
