package com.github.jerryxia.devhelper.requestcapture;

/**
 * @author guqiankun
 *
 */
public interface HttpRequestRecordEventProducer {
    void publish(HttpRequestRecord record);

    void tryPublish(HttpRequestRecord record);
}
