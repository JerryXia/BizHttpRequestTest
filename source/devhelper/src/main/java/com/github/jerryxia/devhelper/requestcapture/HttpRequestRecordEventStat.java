/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Administrator
 *
 */
public class HttpRequestRecordEventStat {

    private final AtomicLong producerSuccessCount = new AtomicLong();
    private final AtomicLong producerFailCount    = new AtomicLong();
    private volatile long    consumerSuccessCount = 0;
    private volatile long    consumerFailCount    = 0;

    public HttpRequestRecordEventStat() {

    }

    public AtomicLong getProducerSuccessCount() {
        return producerSuccessCount;
    }

    public AtomicLong getProducerFailCount() {
        return producerFailCount;
    }

    public long getConsumerSuccessCount() {
        return consumerSuccessCount;
    }

    public long getConsumerFailCount() {
        return consumerFailCount;
    }

    public long incrementProducerSuccessCount() {
        return producerSuccessCount.incrementAndGet();
    }

    public long incrementProducerFailCount() {
        return producerFailCount.incrementAndGet();
    }

    public long incrementConsumerSuccessCount() {
        return ++consumerSuccessCount;
    }

    public long incrementConsumerFailCount() {
        return ++consumerFailCount;
    }

    public void reset() {
        producerSuccessCount.set(0);
        producerFailCount.set(0);
        consumerSuccessCount = 0;
        consumerFailCount = 0;
    }
}
