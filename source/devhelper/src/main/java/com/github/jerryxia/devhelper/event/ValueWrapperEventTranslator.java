/**
 * 
 */
package com.github.jerryxia.devhelper.event;

import com.lmax.disruptor.EventTranslatorOneArg;

public class ValueWrapperEventTranslator implements EventTranslatorOneArg<ValueWrapperEvent, Object> {

    @Override
    public void translateTo(ValueWrapperEvent event, long sequence, Object arg0) {
        event.setValue(arg0);
    }
}
