package org.anonymous.devhelper.requestcapture;

import com.lmax.disruptor.RingBuffer;

/**
 * @author guqiankun
 *
 */
public class HttpRequestRecordEventProducer {
    private final RingBuffer<HttpRequestRecordEvent> ringBuffer;
    private final HttpRequestRecordEventStat         eventStat;

    public HttpRequestRecordEventProducer(RingBuffer<HttpRequestRecordEvent> ringBuffer,
            HttpRequestRecordEventStat httpRequestRecordEventStat) {
        this.ringBuffer = ringBuffer;
        this.eventStat = httpRequestRecordEventStat;
    }

    public void publish(HttpRequestRecord record) {
        long sequence = ringBuffer.next();
        try {
            // 根据序列号获取预分配的数据槽
            HttpRequestRecordEvent event = ringBuffer.get(sequence);
            event.setValue(record);
        } finally {
            ringBuffer.publish(sequence);
            this.eventStat.incrementProducerSuccessCount();
        }
    }
}
