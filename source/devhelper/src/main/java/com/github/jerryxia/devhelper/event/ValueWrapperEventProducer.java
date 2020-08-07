/**
 * 
 */
package com.github.jerryxia.devhelper.event;

public interface ValueWrapperEventProducer {
    void publish(Object record);

    void tryPublish(Object record);
}
