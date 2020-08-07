/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.support;

import java.util.HashMap;

/**
 * @author Administrator
 *
 */
public class RequestCaptureConstants {

    public static volatile boolean          REQUEST_CAPTURE_FILTER_ENABLED = false;
    public static final ThreadLocal<String> HTTP_REQUEST_RECORD_ID         = new ThreadLocal<String>();
    public static final String              REQUEST_CAPTURE_FILTER_ID      = "com.github.jerryxia.devhelper.requestcapture.support.servlet.RequestCaptureFilter.Id";
    public static final int                 DEFAULT_PAYLOAD_LENGTH         = 1024;

    public static volatile boolean               LOG_EXT_ENABLED     = false;
    public static final HashMap<String, Boolean> LOG_EXT_ENABLED_MAP = new HashMap<String, Boolean>();

    static {
        LOG_EXT_ENABLED_MAP.put("log4j", Boolean.FALSE);
        LOG_EXT_ENABLED_MAP.put("logback", Boolean.FALSE);
    }
}
