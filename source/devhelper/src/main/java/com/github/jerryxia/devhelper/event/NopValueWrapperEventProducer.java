/**
 * 
 */
package com.github.jerryxia.devhelper.event;

public class NopValueWrapperEventProducer implements ValueWrapperEventProducer {

    @Override
    public void publish(Object record) {
        // ignore
    }

    @Override
    public void tryPublish(Object record) {
        // ignore
    }

}
