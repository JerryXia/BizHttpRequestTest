/**
 * 
 */
package com.github.jerryxia.devhelper.support.log;

import com.github.jerryxia.devhelper.log.LogEntrySource;

public final class LogConstants {

    public static final ThreadLocal<LogEntrySource> LOG_ENTRY_SOURCE = new ThreadLocal<LogEntrySource>();
    public static final ThreadLocal<String>         RECORD_ID        = new ThreadLocal<String>();

}
