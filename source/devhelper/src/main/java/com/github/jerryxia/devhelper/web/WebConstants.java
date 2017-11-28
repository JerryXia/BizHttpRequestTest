/**
 * 
 */
package com.github.jerryxia.devhelper.web;

import com.github.jerryxia.devhelper.util.SystemClock;

/**
 * @author guqk
 *
 */
public class WebConstants {

    public static final long START_TIMESTAMP = SystemClock.now();

    public static final String REQUEST_ID_RESPONSE_HEADER_NAME = "X-Call-RequestId";
    public static final ThreadLocal<String> X_CALL_REQUEST_ID = new ThreadLocal<String>();

    public static volatile boolean REQUEST_ID_INIT_FILTER_ENABLED = false;
    public static volatile boolean REQUEST_CAPTURE_FILTER_ENABLED = false;

    public static volatile boolean REQUEST_ID_INIT_INTERCEPTOR_ENABLED = false;
    public static volatile boolean REQUEST_RESPONSE_LOG_INTERCEPTOR_ENABLED = false;


    public static final String REPLAY_HTTP_REQUEST_HEADER_NAME = "X-Replay-RequestId";
}
