/**
 * 
 */
package com.github.jerryxia.devhelper.task;

public enum TaskRunRecordType {
    /**
     * 未知
     */
    UNKNOWN("未知", 0),

    /**
     * 常规请求
     */
    NORMAL("常规", 1),

    /**
     * 重放请求
     */
    REPLAY("重放", 2);

    private String desc;
    private int    code;

    private TaskRunRecordType(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }

    public static String getDesc(int code) {
        for (TaskRunRecordType c : TaskRunRecordType.values()) {
            if (c.getCode() == code) {
                return c.desc;
            }
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    public int getCode() {
        return code;
    }
}