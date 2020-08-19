/**
 * 
 */
package com.github.jerryxia.devhelper.support.spring;

import com.github.jerryxia.devhelper.task.TaskRunRecord;

public class SpringConstants {
    public static volatile boolean REQUEST_RESPONSE_LOG_INTERCEPTOR_ENABLED = false;

    public static volatile boolean                 SCHEDULED_TASK_RUN_RECORD_AUTO_INJECT_ENABLED = false;
    public static final ThreadLocal<TaskRunRecord> TASK_RUN_RECORD                               = new ThreadLocal<TaskRunRecord>();
}
