/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture;

/**
 * @author Administrator
 *
 */
public class NopHttpRequestRecordEventProducer implements HttpRequestRecordEventProducer{

    @Override
    public void publish(HttpRequestRecord record) {
        // ignore
    }

    @Override
    public void tryPublish(HttpRequestRecord record) {
        // ignore
    }

}
