/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.support;

import java.util.HashMap;

import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecordManager;

/**
 * @author Administrator
 *
 */
public class RequestCaptureConstants {

    public static final ThreadLocal<String> HTTP_REQUEST_RECORD_ID      = new ThreadLocal<String>();
    public static final String              REQUEST_CAPTURE_FILTER_NAME = "com.github.jerryxia.devhelper.requestcapture.support.servlet.RequestCaptureFilter";
    public static final int                 DEFAULT_PAYLOAD_LENGTH      = 1024;

    public static volatile boolean               LOG_EXT_ENABLED_STATUS = false;
    public static final HashMap<String, Boolean> LOG_EXT_ENABLED_MAP    = new HashMap<String, Boolean>();

    public static final HttpRequestRecordManager RECORD_MANAGER = new HttpRequestRecordManager();

    static {
        LOG_EXT_ENABLED_MAP.put("log4j", Boolean.FALSE);
        LOG_EXT_ENABLED_MAP.put("logback", Boolean.FALSE);
    }
}
