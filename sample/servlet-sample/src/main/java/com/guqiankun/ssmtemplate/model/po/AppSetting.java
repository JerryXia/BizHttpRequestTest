package com.guqiankun.ssmtemplate.model.po;

public class AppSetting {
    private String key;

    private String value;

    private String cnMark;

    /**
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return cn_mark
     */
    public String getCnMark() {
        return cnMark;
    }

    /**
     * @param cnMark
     */
    public void setCnMark(String cnMark) {
        this.cnMark = cnMark;
    }
}