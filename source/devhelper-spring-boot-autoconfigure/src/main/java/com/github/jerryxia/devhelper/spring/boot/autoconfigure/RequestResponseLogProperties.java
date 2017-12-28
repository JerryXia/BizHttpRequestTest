/**
 * 
 */
package com.github.jerryxia.devhelper.spring.boot.autoconfigure;

/**
 * @author guqk
 *
 */
public class RequestResponseLogProperties {

    private Boolean enabled;
    private String logRequestHeaderNames;

    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    public String getLogRequestHeaderNames() {
        return logRequestHeaderNames;
    }
    public void setLogRequestHeaderNames(String logRequestHeaderNames) {
        this.logRequestHeaderNames = logRequestHeaderNames;
    }
}
