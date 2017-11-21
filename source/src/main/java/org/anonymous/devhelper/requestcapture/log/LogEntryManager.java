/**
 * 
 */
package org.anonymous.devhelper.requestcapture.log;

import java.util.concurrent.Executors;

import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * @author Administrator
 *
 */
public class LogEntryManager {
    private Disruptor<LogEntryEvent>  disruptor;
    private LogEntryStorage           logEntryStorage;
    private LogEntryEventStat         logEntryEventStat;
    private LogEntryEventHandler      consumer;

    public LogEntryManager() {
        this.disruptor = new Disruptor<>(new LogEntryEventFactory(), 1024, Executors.defaultThreadFactory(),
                ProducerType.MULTI, new SleepingWaitStrategy());
        this.logEntryStorage = new LogEntryMemoryStorage();
        this.logEntryEventStat = new LogEntryEventStat();
        this.consumer = new LogEntryEventHandler(this.logEntryStorage, this.logEntryEventStat);
    }

    @SuppressWarnings("unchecked")
    public void init() {
        this.disruptor.handleEventsWith(consumer);
        this.disruptor.start();
    }

    public void shutdown() {
        this.disruptor.shutdown();
    }

    public LogEntryEventProducer allocEventProducer() {
        LogEntryEventProducer producer = new LogEntryEventProducer(this.disruptor.getRingBuffer(),
                this.logEntryEventStat);
        return producer;
    }

    public LogEntryEventStat viewLogEntryEventStat() {
        return this.logEntryEventStat;
    }

    public LogEntryStorage currentLogEntryStorage() {
        return this.logEntryStorage;
    }
}
