package com.guqiankun.devhelper.requestcapture;

import java.util.concurrent.atomic.AtomicLong;

public class RecordStat {

    private AtomicLong recordQueueProducerSuccessCount = new AtomicLong();
    private AtomicLong recordQueueProducerFailCount = new AtomicLong();
    private AtomicLong recordQueueProducerInvalidCount = new AtomicLong();
    private long recordQueueConsumerSuccessCount = 0;
    private long recordQueueConsumerFailCount = 0;

    public RecordStat() {
        //
    }

    public AtomicLong getRecordQueueProducerSuccessCount() {
        return recordQueueProducerSuccessCount;
    }

    public AtomicLong getRecordQueueProducerFailCount() {
        return recordQueueProducerFailCount;
    }

    public long getRecordQueueConsumerSuccessCount() {
        return recordQueueConsumerSuccessCount;
    }

    public long getRecordQueueConsumerFailCount() {
        return recordQueueConsumerFailCount;
    }

    public long recordQueueProducerSuccessIncrement() {
        return recordQueueProducerSuccessCount.incrementAndGet();
    }
    public long recordQueueProducerFailIncrement() {
        return recordQueueProducerFailCount.incrementAndGet();
    }
    public long recordQueueProducerInvalidIncrement() {
        return recordQueueProducerInvalidCount.incrementAndGet();
    }
    public long recordQueueConsumerSuccessIncrement() {
        return ++recordQueueConsumerSuccessCount;
    }
    public long recordQueueConsumerFailIncrement() {
        return ++recordQueueConsumerFailCount;
    }

    public void reset() {
        recordQueueProducerSuccessCount.set(0);
        recordQueueProducerFailCount.set(0);
        recordQueueConsumerSuccessCount = 0;
        recordQueueConsumerFailCount = 0;
    }

}
