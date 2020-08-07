/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture;

import com.github.jerryxia.devhelper.event.ValueWrapperEvent;
import com.lmax.disruptor.EventHandler;

/**
 * @author Administrator
 *
 */
public class HttpRequestRecordEventHandler implements EventHandler<ValueWrapperEvent> {

    private final HttpRequestRecordStorage httpRequestRecordStorage;

    public HttpRequestRecordEventHandler(HttpRequestRecordStorage httpRequestRecordStorage) {
        this.httpRequestRecordStorage = httpRequestRecordStorage;
    }

    @Override
    public void onEvent(ValueWrapperEvent event, long sequence, boolean endOfBatch) {
        HttpRequestRecord record = (HttpRequestRecord) event.getValue();
        this.httpRequestRecordStorage.save(record);
    }
}
