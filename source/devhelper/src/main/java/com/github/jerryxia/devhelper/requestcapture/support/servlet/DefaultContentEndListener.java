/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.support.servlet;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecord;

/**
 * @author Administrator
 *
 */
public class DefaultContentEndListener implements ContentEndListener {

    private final HttpRequestRecord httpRequestRecord;

    public DefaultContentEndListener(HttpRequestRecord hrr) {
        this.httpRequestRecord = hrr;
    }

    @Override
    public void handleContentEnd(ByteArrayOutputStream cachedContent, String charsetName) {
        String payload = null;

        byte[] buf = cachedContent.toByteArray();
        if (buf.length > 0) {
            // int length = Math.min(buf.length, 10240);
            int length = buf.length;
            try {
                payload = new String(buf, 0, length, charsetName);
            } catch (UnsupportedEncodingException ex) {
                payload = "[unknown]";
            }
        }

        this.httpRequestRecord.setPayload(payload);
    }
}