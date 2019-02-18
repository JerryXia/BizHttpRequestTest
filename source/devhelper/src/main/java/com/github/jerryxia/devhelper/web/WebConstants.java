/**
 * 
 */
package com.github.jerryxia.devhelper.web;

/**
 * @author guqk
 *
 */
public class WebConstants {
    /**
     * 响应头的名字， 优先读取配置，其次使用默认值
     */
    public static final String REQUEST_ID_RESPONSE_HEADER_NAME = "X-Call-RequestId";
    public static final String REQUEST_ID_INIT_FILTER_ID = "com.github.jerryxia.devhelper.web.filter.RequestIdInitFilter.Id";

    public static volatile boolean REQUEST_ID_INIT_FILTER_ENABLED = false;
    public static volatile boolean REQUEST_RESPONSE_LOG_INTERCEPTOR_ENABLED = false;


    public static final String REPLAY_HTTP_REQUEST_HEADER_NAME = "X-Replay-RequestId";
}
