package com.guqiankun.devhelper.requestcapture;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author guqiankun
 *
 */
public class Constants {

    public static final AtomicLong LOG_ENTRY_ID = new AtomicLong(0);

    public static final ThreadLocal<String> HTTP_REQUEST_RECORD_ID = new ThreadLocal<String>();

    public static final ThreadLocal<String> HTTP_REQUEST_RECORD_REQUEST_ID = new ThreadLocal<String>();

    public final static RecordManager RECORD_MANAGER = new RecordManager();

}
