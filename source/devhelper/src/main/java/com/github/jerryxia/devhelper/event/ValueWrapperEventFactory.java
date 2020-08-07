/**
 * 
 */
package com.github.jerryxia.devhelper.event;

import com.lmax.disruptor.EventFactory;

public class ValueWrapperEventFactory  implements EventFactory<ValueWrapperEvent> {

    @Override
    public ValueWrapperEvent newInstance() {
        return new ValueWrapperEvent();
    }
}
