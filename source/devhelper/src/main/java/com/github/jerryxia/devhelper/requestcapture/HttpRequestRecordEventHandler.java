/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture;

import com.lmax.disruptor.EventHandler;

/**
 * @author Administrator
 *
 */
public class HttpRequestRecordEventHandler implements EventHandler<HttpRequestRecordEvent> {

    private final HttpRequestRecordStorage   httpRequestRecordStorage;
    private final HttpRequestRecordEventStat eventStat;

    public HttpRequestRecordEventHandler(HttpRequestRecordStorage httpRequestRecordStorage,
            HttpRequestRecordEventStat httpRequestRecordEventStat) {
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
