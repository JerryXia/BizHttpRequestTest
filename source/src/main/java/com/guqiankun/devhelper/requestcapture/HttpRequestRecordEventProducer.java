package com.guqiankun.devhelper.requestcapture;

import com.lmax.disruptor.RingBuffer;

/**
 * @author guqiankun
 *
 */
public class HttpRequestRecordEventProducer {

    private final RingBuffer<HttpRequestRecordEvent> ringBuffer;
    private RecordStat recordStat;

    public HttpRequestRecordEventProducer(RingBuffer<HttpRequestRecordEvent> ringBuffer, RecordStat recordStat) {
        this.ringBuffer = ringBuffer;
        this.recordStat = recordStat;
    }

    public void publish(HttpRequestRecord record) {
        long sequence = ringBuffer.next();  // 获取下一个序列号
        try {
            HttpRequestRecordEvent event = ringBuffer.get(sequence); // 根据序列号获取预分配的数据槽
            event.setValue(record);  // 向数据槽中填充数据
        } finally {
            ringBuffer.publish(sequence);
            recordStat.recordQueueProducerSuccessIncrement();
        }
    }
}
