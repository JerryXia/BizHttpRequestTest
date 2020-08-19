/**
 * 
 */
package com.github.jerryxia.devhelper.task;

import java.util.Map;

public class TaskRunRecord {
    private String              id;
    private TaskRunRecordType   type;
    private long                startTimeStamp;
    private long                endTimeStamp;
    private String              declaringClass;
    private String              method;
    private Map<String, Object> parameterMap;
    private String              hostName;
    private String              ip;
    private String              instanceName;

    public TaskRunRecord(String uuid, TaskRunRecordType type, long timeStamp) {
        switch (type) {
        case NORMAL:
        case REPLAY:
            this.id = uuid;
            this.type = type;
            this.startTimeStamp = timeStamp;
            break;
        case UNKNOWN:
        default:
            throw new IllegalArgumentException("Invalid TaskRunRecordType");
        }
    }

    public String getId() {
        return id;
    }

    public TaskRunRecordType getType() {
        return type;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public String getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(String declaringClass) {
        this.declaringClass = declaringClass;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
}
