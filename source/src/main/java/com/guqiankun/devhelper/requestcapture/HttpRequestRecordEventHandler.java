package com.guqiankun.devhelper.requestcapture;

import static com.guqiankun.devhelper.requestcapture.Assert.notNull;

import com.lmax.disruptor.EventHandler;

public class HttpRequestRecordEventHandler implements EventHandler<HttpRequestRecordEvent> {

    private RecordStat    recordStat;
    private RecordStorage recordStorage;

    public HttpRequestRecordEventHandler(RecordStat recordStat, RecordStorage recordStorage) {
        notNull(recordStat);
        notNull(recordStorage);
        this.recordStat = recordStat;
        this.recordStorage = recordStorage;
    }

    @Override
    public void onEvent(HttpRequestRecordEvent event, long sequence, boolean endOfBatch) {
        boolean saveSuccessed = this.recordStorage.save(event.getValue());
        if (saveSuccessed) {
            this.recordStat.recordQueueConsumerSuccessIncrement();
        } else {
            this.recordStat.recordQueueConsumerFailIncrement();
        }
    }
}
