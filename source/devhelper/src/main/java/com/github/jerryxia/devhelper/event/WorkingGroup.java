/**
 * 
 */
package com.github.jerryxia.devhelper.event;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.github.jerryxia.devutil.CustomNameThreadFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class WorkingGroup {
    public static final ValueWrapperEventExceptionHandler DEFAULT_EXCEPTION_HANDLER                 = new ValueWrapperEventExceptionHandler();
    public static final int                               DEFAULT_RINGBUFFER_SIZE                   = 1024;
    public static final long                              DEFAULT_RINGBUFFER_SHUTDOWN_TIMEOUT_MS    = 50;
    public static final NopValueWrapperEventProducer      DEFAULT_NOPHTTPREQUESTRECORDEVENTPRODUCER = new NopValueWrapperEventProducer();

    private final MutiSourceValueWrapperEventStat                    eventStats;
    private final HashMap<Class<?>, EventHandler<ValueWrapperEvent>> consumerEventHandlers;
    private final ValueWrapperEventFactory                           eventFactory;
    private final int                                                ringBufferSize;
    private final CustomNameThreadFactory                            threadFactory;
    private final SleepingWaitStrategy                               waitStrategy;
    private ValueWrapperEventDispatchHandler                         consumer;
    private Disruptor<ValueWrapperEvent>                             disruptor;
    private WorkingValueWrapperEventProducer                         producer;
    private volatile boolean                                         started;

    public WorkingGroup() {
        this.eventStats = new MutiSourceValueWrapperEventStat();
        this.consumerEventHandlers = new HashMap<Class<?>, EventHandler<ValueWrapperEvent>>();
        this.eventFactory = new ValueWrapperEventFactory();
        this.ringBufferSize = DEFAULT_RINGBUFFER_SIZE;
        this.threadFactory = new CustomNameThreadFactory("devhelper", "EventGroup");
        // Processor instruction(such as: a + b), need 2 - 4 ns
        this.waitStrategy = new SleepingWaitStrategy();
        // new TimeoutBlockingWaitStrategy(0, TimeUnit.MILLISECONDS);
    }

    public void registerHandler(Class<?> source, EventHandler<ValueWrapperEvent> eventHandler) {
        this.eventStats.register(source);
        this.consumerEventHandlers.put(source, eventHandler);
    }

    public synchronized void start() {
        if (this.disruptor != null) {
            return;
        }
        // init consumer
        this.consumer = new ValueWrapperEventDispatchHandler(this.eventStats);
        Iterator<Entry<Class<?>, EventHandler<ValueWrapperEvent>>> entrySetIterator = this.consumerEventHandlers.entrySet().iterator();
        while (entrySetIterator.hasNext()) {
            Entry<Class<?>, EventHandler<ValueWrapperEvent>> entry = entrySetIterator.next();
            this.consumer.registerHander(entry.getKey(), entry.getValue());
        }

        // init disruptor
        this.disruptor = new Disruptor<>(eventFactory, ringBufferSize, threadFactory, ProducerType.MULTI, waitStrategy);
        this.disruptor.setDefaultExceptionHandler(DEFAULT_EXCEPTION_HANDLER);
        this.disruptor.handleEventsWith(this.consumer);
        // 而若要消费者不重复的处理生产者的消息，则使用disruptor.handleEventsWithWorkerPool方法将消费者传入
        // this.disruptor.handleEventsWithWorkerPool(workHandlers);

        // init producer
        this.producer = new WorkingValueWrapperEventProducer(this.disruptor.getRingBuffer(), this.eventStats);

        this.disruptor.start();
        setStarted();
    }

    public boolean shutdown() {
        final Disruptor<ValueWrapperEvent> temp = disruptor;
        if (temp == null) {
            // disruptor for this configuration already shut down.
            // disruptor was already shut down by another thread
            return true;
        }

        this.eventStats.resetAll();
        // this.consumerEventHandlers.clear();

        // We must guarantee that publishing to the RingBuffer has stopped before we call disruptor.shutdown().
        // client code fails with NPE if log after stop = OK
        disruptor = null;

        try {
            // busy-spins until all events currently in the disruptor have been processed, or timeout
            temp.shutdown(DEFAULT_RINGBUFFER_SHUTDOWN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (final TimeoutException e) {
            System.err.println("Disruptor shutdown timed out after 50ms");
            temp.halt(); // give up on remaining log events, if any
        }
        setStopped();
        return true;
    }

    public ValueWrapperEventProducer allocEventProducer() {
        if (this.started && this.disruptor != null) {
            return this.producer;
        } else {
            return DEFAULT_NOPHTTPREQUESTRECORDEVENTPRODUCER;
        }
    }

    public MutiSourceValueWrapperEventStat viewEventStats() {
        return this.eventStats;
    }

    public boolean isWorking() {
        return this.started;
    }

    private void setStarted() {
        this.started = true;
    }

    private void setStopped() {
        this.started = false;
    }

}
