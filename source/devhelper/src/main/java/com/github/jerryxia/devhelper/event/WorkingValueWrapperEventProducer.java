/**
 * 
 */
package com.github.jerryxia.devhelper.event;

import com.lmax.disruptor.RingBuffer;

public class WorkingValueWrapperEventProducer implements ValueWrapperEventProducer {
    private static final ValueWrapperEventTranslator TRANSLATOR = new ValueWrapperEventTranslator();

    private final RingBuffer<ValueWrapperEvent>   ringBuffer;
    private final MutiSourceValueWrapperEventStat eventStats;

    public WorkingValueWrapperEventProducer(RingBuffer<ValueWrapperEvent> ringBuffer, MutiSourceValueWrapperEventStat eventStats) {
        if (ringBuffer == null) {
            throw new IllegalArgumentException("parameter:'ringBuffer' can't be null.");
        }
        if (eventStats == null) {
            throw new IllegalArgumentException("parameter:'eventStats' can't be null.");
        }
        this.ringBuffer = ringBuffer;
        this.eventStats = eventStats;
    }

    @Override
    public void publish(Object record) {
        Class<?> source = record.getClass();
        ringBuffer.publishEvent(TRANSLATOR, record);
        eventStats.allocate(source).incrementProducerSuccessCount();
    }

    @Override
    public void tryPublish(Object record) {
        Class<?> source = record.getClass();
        if (ringBuffer.tryPublishEvent(TRANSLATOR, record)) {
            eventStats.allocate(source).incrementProducerSuccessCount();
        } else {
            eventStats.allocate(source).incrementProducerFailCount();
        }
    }

}
