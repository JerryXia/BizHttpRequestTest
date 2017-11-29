/**
 * 
 */
package com.github.jerryxia.devhelper.spring.boot.autoconfigure;

/**
 * @author guqk
 *
 */
public class RequestResponseLogProperties {

    private Boolean enable;
    private String logRequestHeaderNames;


    public Boolean getEnable() {
        return enable;
    }
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
    public String getLogRequestHeaderNames() {
        return logRequestHeaderNames;
    }
    public void setLogRequestHeaderNames(String logRequestHeaderNames) {
        this.logRequestHeaderNames = logRequestHeaderNames;
    }
}
