package com.github.jerryxia.devhelper.requestcapture;

import java.util.concurrent.Executors;

import com.github.jerryxia.devhelper.requestcapture.log.LogEntryManager;

import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * @author guqiankun
 *
 */
public class HttpRequestRecordManager {

    private Disruptor<HttpRequestRecordEvent> disruptor;
    private HttpRequestRecordStorage          recordStorage;
    private HttpRequestRecordEventStat        eventStat;
    private HttpRequestRecordEventHandler     consumer;
    private LogEntryManager                   logEntryManager;

    public HttpRequestRecordManager() {
        this.disruptor = new Disruptor<>(new HttpRequestRecordEventFactory(), 1024, Executors.defaultThreadFactory(),
                ProducerType.MULTI, new SleepingWaitStrategy());
        this.recordStorage = new HttpRequestRecordMemoryStorage();
        this.eventStat = new HttpRequestRecordEventStat();
        this.consumer = new HttpRequestRecordEventHandler(this.recordStorage, this.eventStat);
        this.logEntryManager = new LogEntryManager();
    }

    @SuppressWarnings("unchecked")
    public void init() {
        disruptor.handleEventsWith(this.consumer);
        disruptor.start();
        this.logEntryManager.init();
    }

    public void shutdown() {
        disruptor.shutdown();
        this.logEntryManager.shutdown();
    }

    public HttpRequestRecordEventProducer allocEventProducer() {
        HttpRequestRecordEventProducer producer = new HttpRequestRecordEventProducer(this.disruptor.getRingBuffer(),
                this.eventStat);
        return producer;
    }

    public HttpRequestRecordEventStat viewHttpRequestRecordEventStat() {
        return this.eventStat;
    }

    public HttpRequestRecordStorage currentHttpRequestRecordStorage() {
        return this.recordStorage;
    }

    public LogEntryManager currentLogEntryManager() {
        return this.logEntryManager;
    }
}
