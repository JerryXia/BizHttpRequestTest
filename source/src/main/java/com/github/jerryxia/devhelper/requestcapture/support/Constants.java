/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture.support;

import java.util.HashMap;
import java.util.Map;

import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecordManager;

/**
 * @author Administrator
 *
 */
public class Constants {

    public static final ThreadLocal<String> HTTP_REQUEST_RECORD_ID = new ThreadLocal<String>();
    public static final ThreadLocal<String> HTTP_REQUEST_RECORD_REPLAYING_REQUEST_ID = new ThreadLocal<String>();

    public static boolean LOG_EXT_ENABLED_STATUS = false;
    public static final Map<String, Boolean> LOG_EXT_ENABLED_MAP = new HashMap<String, Boolean>();

    public static final HttpRequestRecordManager RECORD_MANAGER = new HttpRequestRecordManager();

    static {
        LOG_EXT_ENABLED_MAP.put("log4j", Boolean.FALSE);
        LOG_EXT_ENABLED_MAP.put("logback", Boolean.FALSE);
    }
}
