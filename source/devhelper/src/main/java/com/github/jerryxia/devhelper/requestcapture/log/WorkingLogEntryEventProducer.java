/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.log;

import com.lmax.disruptor.RingBuffer;

/**
 * @author Administrator
 *
 */
public class WorkingLogEntryEventProducer implements LogEntryEventProducer {
    public static final LogEntryEventTranslator TRANSLATOR = new LogEntryEventTranslator();

    private final RingBuffer<LogEntryEvent> ringBuffer;
    private final LogEntryEventStat         eventStat;

    public WorkingLogEntryEventProducer(RingBuffer<LogEntryEvent> ringBuffer, LogEntryEventStat logEntryEventStat) {
        this.ringBuffer = ringBuffer;
        this.eventStat = logEntryEventStat;
    }

    @Override
    public void publish(LogEntry record) {
        ringBuffer.publishEvent(TRANSLATOR, record);
        this.eventStat.incrementProducerSuccessCount();
    }

    @Override
    public void tryPublish(LogEntry record) {
        if (ringBuffer.tryPublishEvent(TRANSLATOR, record)) {
            this.eventStat.incrementProducerSuccessCount();
        } else {
            this.eventStat.incrementProducerFailCount();
        }
    }
}
