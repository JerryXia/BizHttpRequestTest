/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.log;

import com.lmax.disruptor.RingBuffer;

/**
 * @author Administrator
 *
 */
public class LogEntryEventProducer {
    private final RingBuffer<LogEntryEvent> ringBuffer;
    private final LogEntryEventStat         eventStat;

    public LogEntryEventProducer(RingBuffer<LogEntryEvent> ringBuffer, LogEntryEventStat logEntryEventStat) {
        this.ringBuffer = ringBuffer;
        this.eventStat = logEntryEventStat;
    }

    public void publish(LogEntry record) {
        long sequence = ringBuffer.next();
        try {
            // 根据序列号获取预分配的数据槽
            LogEntryEvent event = ringBuffer.get(sequence);
            event.setValue(record);
        } finally {
            ringBuffer.publish(sequence);
            this.eventStat.incrementProducerSuccessCount();
        }
    }
}
