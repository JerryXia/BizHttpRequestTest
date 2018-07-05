/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture;

import com.lmax.disruptor.RingBuffer;

/**
 * @author Administrator
 *
 */
public class WorkingHttpRequestRecordEventProducer implements HttpRequestRecordEventProducer{
    private static final HttpRequestRecordEventTranslator TRANSLATOR = new HttpRequestRecordEventTranslator();

    private final RingBuffer<HttpRequestRecordEvent> ringBuffer;
    private final HttpRequestRecordEventStat         eventStat;

    public WorkingHttpRequestRecordEventProducer(RingBuffer<HttpRequestRecordEvent> ringBuffer,
            HttpRequestRecordEventStat httpRequestRecordEventStat) {
        this.ringBuffer = ringBuffer;
        this.eventStat = httpRequestRecordEventStat;
    }

    @Override
    public void publish(HttpRequestRecord record) {
        ringBuffer.publishEvent(TRANSLATOR, record);
        this.eventStat.incrementProducerSuccessCount();
    }

    @Override
    public void tryPublish(HttpRequestRecord record) {
        if (ringBuffer.tryPublishEvent(TRANSLATOR, record)) {
            this.eventStat.incrementProducerSuccessCount();
        } else {
            this.eventStat.incrementProducerFailCount();
        }
    }
}
