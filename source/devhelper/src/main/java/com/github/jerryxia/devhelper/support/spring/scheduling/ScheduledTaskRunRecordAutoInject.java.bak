/**
 * 
 */
package com.github.jerryxia.devhelper.support.spring.scheduling;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
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

//@org.aspectj.lang.annotation.Aspect
public class ScheduledTaskRunRecordAutoInject {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTaskRunRecordAutoInject.class);
    // private static final String SCHEDULED_METHOD_POINTCUT_EXPRESSION =
    // "@annotation(org.springframework.scheduling.annotation.Scheduled)";

    private InetAddress localHost;
    private String      hostName;
    private String      ip;
    private String      instanceName;
    private boolean     lazyMode = true;

    public ScheduledTaskRunRecordAutoInject() {
        try {
            localHost = InetAddress.getLocalHost();
            hostName = localHost.getHostName();
            ip = localHost.getHostAddress();
        } catch (UnknownHostException e) {
            hostName = "UnKnown";
            ip = "0.0.0.0";
        }
        SpringConstants.SCHEDULED_TASK_RUN_RECORD_AUTO_INJECT_ENABLED = true;
    }

    public void init() {
        if (log.isInfoEnabled()) {
            log.info("devhelper ScheduledTaskRunRecordAutoInject enabled: {}, lazyMode: {}", SpringConstants.SCHEDULED_TASK_RUN_RECORD_AUTO_INJECT_ENABLED, this.lazyMode);
        }
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public void setLazyMode(boolean lazyMode) {
        this.lazyMode = lazyMode;
    }

    // @org.aspectj.lang.annotation.Around(SCHEDULED_METHOD_POINTCUT_EXPRESSION)
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> runningClass = joinPoint.getTarget().getClass();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method runningMethod = signature.getMethod();

        TaskRunRecord taskRunRecord = buildTaskRunRecord(runningClass.getName(), runningMethod.getName(), null);
        // trace
        LogConstants.LOG_ENTRY_SOURCE.set(LogEntrySource.TASK_SCHEDULE);
        LogConstants.RECORD_ID.set(taskRunRecord.getId());
        // SpringConstants.TASK_RUN_RECORD.set(taskRunRecord);

        Constants.TASK_RUN_RECORD_MANAGER.allocEventProducer().tryPublish(taskRunRecord);
        try {
            if (log.isTraceEnabled()) {
                log.trace("@Around.0 {}#{}", runningClass.getName(), runningMethod.getName());
            }
            Object processedResult = joinPoint.proceed();
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
            // remove trace info
            // SpringConstants.TASK_RUN_RECORD.remove();
            // LogConstants.RECORD_ID.remove();
            // LogConstants.LOG_ENTRY_SOURCE.remove();
        }
    }

    // @org.aspectj.lang.annotation.Before(SCHEDULED_METHOD_POINTCUT_EXPRESSION)
    public void doBefore(JoinPoint joinPoint) {
        Class<?> runningClass = joinPoint.getTarget().getClass();
        String runningMethodName = joinPoint.getSignature().getName();

        TaskRunRecord taskRunRecord = buildTaskRunRecord(runningClass.getName(), runningMethodName, null);
        // trace
        LogConstants.LOG_ENTRY_SOURCE.set(LogEntrySource.TASK_SCHEDULE);
        LogConstants.RECORD_ID.set(taskRunRecord.getId());
        SpringConstants.TASK_RUN_RECORD.set(taskRunRecord);

        Constants.TASK_RUN_RECORD_MANAGER.allocEventProducer().tryPublish(taskRunRecord);

        if (log.isTraceEnabled()) {
            log.trace("@Before {}#{}", runningClass.getName(), runningMethodName);
        }
    }

    // @org.aspectj.lang.annotation.@After(SCHEDULED_METHOD_POINTCUT_EXPRESSION)
    public void doAfter(JoinPoint joinPoint) {
        TaskRunRecord taskRunRecord = SpringConstants.TASK_RUN_RECORD.get();
        if (taskRunRecord != null) {
            taskRunRecord.setEndTimeStamp(SystemClock.now());
        }

        if (log.isTraceEnabled()) {
            Class<?> runningClass = joinPoint.getTarget().getClass();
            String runningMethodName = joinPoint.getSignature().getName();
            log.trace("@After {}#{}", runningClass.getName(), runningMethodName);
        }
    }

    // @org.aspectj.lang.annotation.AfterReturning(SCHEDULED_METHOD_POINTCUT_EXPRESSION)
    public void doAfterReturning(JoinPoint joinPoint, Object retVal) {
        if (log.isTraceEnabled()) {
            Class<?> runtimeClass = joinPoint.getTarget().getClass();
            String methodName = joinPoint.getSignature().getName();
            log.trace("@AfterReturning {}#{}, returning-> {}", runtimeClass.getName(), methodName, retVal);
        }

        SpringConstants.TASK_RUN_RECORD.remove();
        if (this.lazyMode) {
            // capture current thread's all logs after.
        } else {
            LogConstants.RECORD_ID.remove();
            LogConstants.LOG_ENTRY_SOURCE.remove();
        }
    }

    // @org.aspectj.lang.annotation.AfterThrowing(SCHEDULED_METHOD_POINTCUT_EXPRESSION)
    public void doAfterThrowing(JoinPoint joinPoint, Throwable t) {
        if (log.isTraceEnabled()) {
            Class<?> runtimeClass = joinPoint.getTarget().getClass();
            String methodName = joinPoint.getSignature().getName();
            log.trace(String.format("@AfterThrowing %s#%s, throwing", runtimeClass.getName(), methodName), t);
        }
        // org.springframework.scheduling.support.DelegatingErrorHandlingRunnable
        // org.springframework.scheduling.support.TaskUtils

        SpringConstants.TASK_RUN_RECORD.remove();
        if (this.lazyMode) {
            // capture current thread's all logs after.
        } else {
            LogConstants.RECORD_ID.remove();
            LogConstants.LOG_ENTRY_SOURCE.remove();
        }
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
        taskRunRecord.setInstanceName(this.instanceName);
        return taskRunRecord;
    }
}