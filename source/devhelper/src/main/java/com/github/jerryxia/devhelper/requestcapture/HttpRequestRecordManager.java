package com.github.jerryxia.devhelper.requestcapture;

import java.util.concurrent.TimeUnit;

import com.github.jerryxia.devhelper.requestcapture.log.LogEntryManager;
import com.github.jerryxia.devutil.CustomNameThreadFactory;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * @author guqiankun
 *
 */
public class HttpRequestRecordManager {
    private final HttpRequestRecordStorage      recordStorage;
    private final HttpRequestRecordEventStat    eventStat;
    private final HttpRequestRecordEventHandler consumer;

    private final HttpRequestRecordEventFactory   eventFactory;
    private final int                             ringBufferSize;
    private final CustomNameThreadFactory         threadFactory;
    private final SleepingWaitStrategy            waitStrategy;
    private Disruptor<HttpRequestRecordEvent>     disruptor;
    private WorkingHttpRequestRecordEventProducer producer;
    private volatile boolean                      started;

    private final LogEntryManager logEntryManager;

    public HttpRequestRecordManager() {
        // init consumer
        this.recordStorage = new HttpRequestRecordMemoryStorage();
        this.eventStat = new HttpRequestRecordEventStat();
        this.consumer = new HttpRequestRecordEventHandler(this.recordStorage, this.eventStat);

        this.eventFactory = new HttpRequestRecordEventFactory();
        this.ringBufferSize = RequestCaptureConstants.DEFAULT_RINGBUFFER_SIZE;
        this.threadFactory = new CustomNameThreadFactory("devhelper", "RequestCapture");
        this.waitStrategy = new SleepingWaitStrategy();
        // new TimeoutBlockingWaitStrategy(0, TimeUnit.MILLISECONDS);

        this.logEntryManager = new LogEntryManager();
        this.logEntryManager.start();
    }

    public synchronized void start() {
        if (this.disruptor != null) {
            return;
        }
        // init disruptor
        this.disruptor = new Disruptor<>(eventFactory, ringBufferSize, threadFactory, ProducerType.MULTI, waitStrategy);
        this.disruptor.handleEventsWith(this.consumer);
        // 而若要消费者不重复的处理生产者的消息，则使用disruptor.handleEventsWithWorkerPool方法将消费者传入
        // this.disruptor.handleEventsWithWorkerPool(workHandlers);
        this.disruptor.setDefaultExceptionHandler(RequestCaptureConstants.DEFAULT_EXCEPTION_HANDLER);
        disruptor.start();
        // init producer
        this.producer = new WorkingHttpRequestRecordEventProducer(this.disruptor.getRingBuffer(), this.eventStat);
        setStarted();
    }

    public boolean shutdown() {
        final Disruptor<HttpRequestRecordEvent> temp = disruptor;
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
            temp.shutdown(RequestCaptureConstants.DEFAULT_RINGBUFFER_SHUTDOWN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (final TimeoutException e) {
            System.err.println("Disruptor shutdown timed out after 50ms");
            temp.halt(); // give up on remaining log events, if any
        }
        setStopped();
        this.logEntryManager.shutdown();
        return true;
    }

    public HttpRequestRecordEventProducer allocEventProducer() {
        if (this.started && this.disruptor != null) {
            return this.producer;
        } else {
            return RequestCaptureConstants.DEFAULT_NOPHTTPREQUESTRECORDEVENTPRODUCER;
        }
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
