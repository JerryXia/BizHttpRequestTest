/**
 * 
 */
package com.github.jerryxia.devhelper.support.spring.scheduling;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jerryxia.devhelper.Constants;
import com.github.jerryxia.devhelper.log.LogEntrySource;
import com.github.jerryxia.devhelper.support.log.LogConstants;
import com.github.jerryxia.devhelper.support.spring.SpringConstants;
import com.github.jerryxia.devhelper.task.TaskRunRecord;
import com.github.jerryxia.devhelper.task.TaskRunRecordType;
import com.github.jerryxia.devutil.ObjectId;
import com.github.jerryxia.devutil.SystemClock;

/**
 * @author Administrator
 *
 */
public class ScheduledTaskRunRecordAutoInjectInterceptor implements MethodInterceptor {
    private InetAddress localHost;
    private String      hostName;
    private String      ip;
    private String      instanceName;
    private String      subInstanceName;
    private boolean     lazyMode = true;

    public ScheduledTaskRunRecordAutoInjectInterceptor() {
        try {
            localHost = InetAddress.getLocalHost();
            hostName = localHost.getHostName();
            ip = localHost.getHostAddress();
        } catch (UnknownHostException e) {
            hostName = "UnKnown";
            ip = "0.0.0.0";
        }
        this.subInstanceName = this.hostName + "(" + this.ip + ")";
        SpringConstants.SCHEDULED_TASK_RUN_RECORD_AUTO_INJECT_ENABLED = true;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object target = invocation.getThis();
        Logger log = LoggerFactory.getLogger(target.getClass());

        Method runningMethod = invocation.getMethod();
        Class<?> runningClass = runningMethod.getDeclaringClass();
        if (runningClass.isInstance(target)) {
            runningClass = target.getClass();
        }

        TaskRunRecord taskRunRecord = buildTaskRunRecord(runningClass.getName(), runningMethod.getName(), null);
        // trace
        LogConstants.RECORD_ID.set(taskRunRecord.getId());
        LogConstants.LOG_ENTRY_SOURCE.set(LogEntrySource.TASK_SCHEDULE);

        Constants.TASK_RUN_RECORD_MANAGER.allocEventProducer().tryPublish(taskRunRecord);
        try {
            if (log.isTraceEnabled()) {
                log.trace("@Around.0 {}#{}", runningClass.getName(), runningMethod.getName());
            }
            Object processedResult = invocation.proceed();
            if (log.isTraceEnabled()) {
                log.trace("@Around.1 {}#{}", runningClass.getName(), runningMethod.getName());
            }
            return processedResult;
        } finally {
            if (taskRunRecord != null) {
                taskRunRecord.setEndTimeStamp(SystemClock.now());
            }
            if (log.isTraceEnabled()) {
                log.trace("@Around.2 {}#{}", runningClass.getName(), runningMethod.getName());
            }

            if (this.lazyMode) {
                // capture current thread's all logs after.
            } else {
                LogConstants.RECORD_ID.remove();
                LogConstants.LOG_ENTRY_SOURCE.remove();
            }
        }
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public void setLazyMode(boolean lazyMode) {
        this.lazyMode = lazyMode;
    }

    private TaskRunRecord buildTaskRunRecord(String declaringClass, String method, Map<String, Object> parameterMap) {
        String id = ObjectId.get().toString();
        long timeStamp = SystemClock.now();

        TaskRunRecordType type = TaskRunRecordType.NORMAL;
        // String replayReqId = httpRequest.getHeader(replayRequestIdRequestHeaderName);
        // if (replayReqId != null && replayReqId.length() > 0) {
        // type = HttpRequestRecordType.REPLAY;
        // } else {
        // type = HttpRequestRecordType.NORMAL;
        // }

        TaskRunRecord taskRunRecord = new TaskRunRecord(id, type, timeStamp);
        taskRunRecord.setDeclaringClass(declaringClass);
        taskRunRecord.setMethod(method);
        taskRunRecord.setParameterMap(parameterMap);
        taskRunRecord.setHostName(this.hostName);
        taskRunRecord.setIp(this.ip);
        taskRunRecord.setInstanceName(this.instanceName != null ? this.instanceName : this.subInstanceName);
        return taskRunRecord;
    }

}
