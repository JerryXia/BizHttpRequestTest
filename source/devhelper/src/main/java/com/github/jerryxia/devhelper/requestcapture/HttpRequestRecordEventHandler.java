/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture;

import com.lmax.disruptor.EventHandler;

import com.github.jerryxia.devhelper.util.Assert;

/**
 * @author Administrator
 *
 */
public class HttpRequestRecordEventHandler implements EventHandler<HttpRequestRecordEvent> {

    private final HttpRequestRecordStorage   httpRequestRecordStorage;
    private final HttpRequestRecordEventStat eventStat;

    public HttpRequestRecordEventHandler(HttpRequestRecordStorage httpRequestRecordStorage,
            HttpRequestRecordEventStat httpRequestRecordEventStat) {
        Assert.notNull(httpRequestRecordStorage);
        Assert.notNull(httpRequestRecordEventStat);
        this.httpRequestRecordStorage = httpRequestRecordStorage;
        this.eventStat = httpRequestRecordEventStat;
    }

    @Override
    public void onEvent(HttpRequestRecordEvent event, long sequence, boolean endOfBatch) {
        boolean saveSuccessed = this.httpRequestRecordStorage.save(event.getValue());
        if (saveSuccessed) {
            this.eventStat.incrementConsumerSuccessCount();
        } else {
            this.eventStat.incrementConsumerFailCount();
        }
    }
}
