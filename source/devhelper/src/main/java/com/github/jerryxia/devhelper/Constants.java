/**
 * 
 */
package com.github.jerryxia.devhelper;

import com.github.jerryxia.devhelper.event.WorkingGroup;
import com.github.jerryxia.devhelper.log.LogEntry;
import com.github.jerryxia.devhelper.log.LogEntryManager;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecord;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecordManager;

/**
 * @author Administrator
 *
 */
public final class Constants {
    public static long   START_TIME = 0;
    public static String VERSION    = null;

    public static String SERVER_OS_NAME  = null;
    public static String JAVA_VM_NAME    = null;
    public static String JAVA_VERSION    = null;
    public static String JAVA_HOME       = null;
    public static String JAVA_CLASS_PATH = null;
    public static String JAVA_IO_TMPDIR  = null;

    public static WorkingGroup             EVENT_WORKING_GROUP         = null;
    public static LogEntryManager          LOG_ENTRY_MANAGER           = null;
    public static HttpRequestRecordManager HTTP_REQUEST_RECORD_MANAGER = null;

    static {
        LogEntryManager logEntryManager = new LogEntryManager();
        HttpRequestRecordManager httpRequestRecordManager = new HttpRequestRecordManager();

        WorkingGroup eventWorkingGroup = new WorkingGroup();
        eventWorkingGroup.registerHandler(LogEntry.class, logEntryManager.currentConsumer());
        eventWorkingGroup.registerHandler(HttpRequestRecord.class, httpRequestRecordManager.currentConsumer());
        eventWorkingGroup.start();

        logEntryManager.setEventWorkingGroup(eventWorkingGroup);
        httpRequestRecordManager.setEventWorkingGroup(eventWorkingGroup);

        EVENT_WORKING_GROUP = eventWorkingGroup;
        LOG_ENTRY_MANAGER = logEntryManager;
        HTTP_REQUEST_RECORD_MANAGER = httpRequestRecordManager;
    }

}
