package com.github.jerryxia.devhelper.requestcapture;

import com.lmax.disruptor.EventFactory;

public class HttpRequestRecordEventFactory implements EventFactory<HttpRequestRecordEvent> {

    @Override
    public HttpRequestRecordEvent newInstance() {
        return new HttpRequestRecordEvent();
    }
}