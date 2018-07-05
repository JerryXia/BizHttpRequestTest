package com.github.jerryxia.devhelper.requestcapture.log;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author guqiankun
 *
 */
class LogConstants {

    public static final AtomicLong LOG_ENTRY_ID = new AtomicLong(0);

    public static final LogEntryEventExceptionHandler DEFAULT_EXCEPTION_HANDLER              = new LogEntryEventExceptionHandler();
    public static final int                           DEFAULT_RINGBUFFER_SIZE                = 1024;
    public static final long                          DEFAULT_RINGBUFFER_SHUTDOWN_TIMEOUT_MS = 50;
    public static final NopLogEntryEventProducer      DEFAULT_NOPLOGENTRYEVENTPRODUCER       = new NopLogEntryEventProducer();
}
