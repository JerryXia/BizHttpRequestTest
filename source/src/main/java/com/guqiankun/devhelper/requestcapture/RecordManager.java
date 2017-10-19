package com.guqiankun.devhelper.requestcapture;

import java.util.concurrent.Executors;

import com.guqiankun.devhelper.requestcapture.log.LogEntryList;
import com.guqiankun.devhelper.requestcapture.storage.MemoryStorage;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * @author guqiankun
 *
 */
public class RecordManager {

    private Disruptor<HttpRequestRecordEvent> disruptor;
    private HttpRequestRecordEventHandler     consumer;
    private RecordStat                        recordStat;
    private RecordStorage                     recordStorage;
    private LogEntryList                      recordLogList;

    public RecordManager() {
        this.recordStorage = new MemoryStorage();
        this.recordStat = new RecordStat();
        this.recordLogList = new LogEntryList();
        this.disruptor = new Disruptor<>(new HttpRequestRecordEventFactory(), 1024, Executors.defaultThreadFactory(),
                ProducerType.MULTI, new SleepingWaitStrategy());
        this.consumer = new HttpRequestRecordEventHandler(this.recordStat, this.recordStorage);
    }

    @SuppressWarnings("unchecked")
    public void init() {
        disruptor.handleEventsWith(consumer);
        disruptor.start();
    }

    public void shutdown() {
        disruptor.shutdown();
    }

    public HttpRequestRecordEventProducer allocEventProducer() {
        HttpRequestRecordEventProducer producer = new HttpRequestRecordEventProducer(disruptor.getRingBuffer(),
                this.recordStat);
        return producer;
    }

    public RecordStat viewRecordStat() {
        return recordStat;
    }

    public RecordStorage currentRecordStorage() {
        return recordStorage;
    }

    public LogEntryList currentRecordLogList() {
        return recordLogList;
    }
}
