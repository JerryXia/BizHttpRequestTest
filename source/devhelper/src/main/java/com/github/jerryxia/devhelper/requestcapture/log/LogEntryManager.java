/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.log;

import java.util.concurrent.TimeUnit;

import com.github.jerryxia.devutil.CustomNameThreadFactory;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * @author Administrator
 *
 */
public class LogEntryManager {
    private final LogEntryStorage      logEntryStorage;
    private final LogEntryEventStat    logEntryEventStat;
    private final LogEntryEventHandler consumer;

    private final LogEntryEventFactory    eventFactory;
    private final int                     ringBufferSize;
    private final CustomNameThreadFactory threadFactory;
    private final SleepingWaitStrategy    waitStrategy;
    private Disruptor<LogEntryEvent>      disruptor;
    private WorkingLogEntryEventProducer  producer;
    private volatile boolean              started;

    public LogEntryManager() {
        // init consumer
        this.logEntryStorage = new LogEntryMemoryStorage();
        this.logEntryEventStat = new LogEntryEventStat();
        this.consumer = new LogEntryEventHandler(this.logEntryStorage, this.logEntryEventStat);

        this.eventFactory = new LogEntryEventFactory();
        this.ringBufferSize = LogConstants.DEFAULT_RINGBUFFER_SIZE;
        this.threadFactory = new CustomNameThreadFactory("devhelper", "RequestCaptureLog");
        this.waitStrategy = new SleepingWaitStrategy();
    }

    public synchronized void start() {
        if (this.disruptor != null) {
            return;
        }
        // init disruptor
        this.disruptor = new Disruptor<>(eventFactory, ringBufferSize, threadFactory, ProducerType.MULTI, waitStrategy);
        this.disruptor.handleEventsWith(this.consumer);
        this.disruptor.setDefaultExceptionHandler(LogConstants.DEFAULT_EXCEPTION_HANDLER);
        this.disruptor.start();
        // init producer
        this.producer = new WorkingLogEntryEventProducer(this.disruptor.getRingBuffer(), this.logEntryEventStat);
        setStarted();
    }

    public boolean shutdown() {
        final Disruptor<LogEntryEvent> temp = disruptor;
        if (temp == null) {
            // disruptor for this configuration already shut down.
            // disruptor was already shut down by another thread
            return true;
        }

        // We must guarantee that publishing to the RingBuffer has stopped before we call disruptor.shutdown().
        // client code fails with NPE if log after stop = OK
        disruptor = null;

        try {
            // busy-spins until all events currently in the disruptor have been processed, or timeout
            temp.shutdown(LogConstants.DEFAULT_RINGBUFFER_SHUTDOWN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (final TimeoutException e) {
            System.err.println("Disruptor shutdown timed out after 50ms");
            temp.halt(); // give up on remaining log events, if any
        }
        setStopped();
        return true;
    }

    public LogEntryEventProducer allocEventProducer() {
        if (this.started && this.disruptor != null) {
            return this.producer;
        } else {
            return LogConstants.DEFAULT_NOPLOGENTRYEVENTPRODUCER;
        }
    }

    public LogEntryEventStat viewLogEntryEventStat() {
        return this.logEntryEventStat;
    }

    public LogEntryStorage currentLogEntryStorage() {
        return this.logEntryStorage;
    }

    public boolean isWorking() {
        return started;
    }


    private void setStarted() {
        this.started = true;
    }

    private void setStopped() {
        this.started = false;
    }
}
