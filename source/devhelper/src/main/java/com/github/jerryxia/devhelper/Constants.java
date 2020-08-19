/**
 * 
 */
package com.github.jerryxia.devhelper;

import java.util.HashMap;

import com.github.jerryxia.devhelper.event.WorkingGroup;
import com.github.jerryxia.devhelper.log.LogEntry;
import com.github.jerryxia.devhelper.log.LogEntryManager;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecord;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecordManager;
import com.github.jerryxia.devhelper.support.log.LogProvider;
import com.github.jerryxia.devhelper.task.TaskRunRecord;
import com.github.jerryxia.devhelper.task.TaskRunRecordManager;

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

    public static volatile boolean               LOG_EXT_ENABLED     = false;
    public static final HashMap<String, Boolean> LOG_EXT_ENABLED_MAP = new HashMap<String, Boolean>();

    public static WorkingGroup             EVENT_WORKING_GROUP         = null;
    public static LogEntryManager          LOG_ENTRY_MANAGER           = null;
    public static HttpRequestRecordManager HTTP_REQUEST_RECORD_MANAGER = null;
    public static TaskRunRecordManager     TASK_RUN_RECORD_MANAGER     = null;

    static {
        LOG_EXT_ENABLED_MAP.put(LogProvider.log4j.name(), Boolean.FALSE);
        LOG_EXT_ENABLED_MAP.put(LogProvider.logback.name(), Boolean.FALSE);

        // Initializing EventWorkingGroup before logappender enabled
        LogEntryManager logEntryManager = new LogEntryManager();
        HttpRequestRecordManager httpRequestRecordManager = new HttpRequestRecordManager();
        TaskRunRecordManager taskRunRecordManager = new TaskRunRecordManager();

        WorkingGroup eventWorkingGroup = new WorkingGroup();
        eventWorkingGroup.registerHandler(LogEntry.class, logEntryManager.currentConsumer());
        eventWorkingGroup.registerHandler(HttpRequestRecord.class, httpRequestRecordManager.currentConsumer());
        eventWorkingGroup.registerHandler(TaskRunRecord.class, taskRunRecordManager.currentConsumer());
        eventWorkingGroup.start();

        logEntryManager.setEventWorkingGroup(eventWorkingGroup);
        httpRequestRecordManager.setEventWorkingGroup(eventWorkingGroup);
        taskRunRecordManager.setEventWorkingGroup(eventWorkingGroup);

        Constants.EVENT_WORKING_GROUP = eventWorkingGroup;
        Constants.LOG_ENTRY_MANAGER = logEntryManager;
        Constants.HTTP_REQUEST_RECORD_MANAGER = httpRequestRecordManager;
        Constants.TASK_RUN_RECORD_MANAGER = taskRunRecordManager;
    }
}
