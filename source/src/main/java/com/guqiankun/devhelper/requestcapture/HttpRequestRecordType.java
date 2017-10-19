package com.guqiankun.devhelper.requestcapture;

public enum HttpRequestRecordType {

    UNKNOWN("未知", 0), NORMAL("常规请求", 1), REPLAY("重放请求", 2);

    private String name;
    private int value;

    private HttpRequestRecordType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static String getName(int value) {
        for (HttpRequestRecordType c : HttpRequestRecordType.values()) {
            if (c.getValue() == value) {
                return c.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
