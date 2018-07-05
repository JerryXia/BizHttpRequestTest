/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture;

import com.lmax.disruptor.EventTranslatorOneArg;

/**
 * @author Administrator
 *
 */
public class HttpRequestRecordEventTranslator
        implements EventTranslatorOneArg<HttpRequestRecordEvent, HttpRequestRecord> {

    @Override
    public void translateTo(HttpRequestRecordEvent event, long sequence, HttpRequestRecord arg0) {
        event.setValue(arg0);
    }
}
