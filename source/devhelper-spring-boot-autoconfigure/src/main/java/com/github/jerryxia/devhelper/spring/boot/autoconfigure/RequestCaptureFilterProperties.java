/**
 * 
 */
package com.github.jerryxia.devhelper.spring.boot.autoconfigure;

/**
 * @author Administrator
 *
 */
public class RequestCaptureFilterProperties {
    private Boolean enabled;
    private String exclusions;
    private String replayRequestIdRequestHeaderName;

    public Boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    public String getExclusions() {
        return exclusions;
    }
    public void setExclusions(String exclusions) {
        this.exclusions = exclusions;
    }
    public String getReplayRequestIdRequestHeaderName() {
        return replayRequestIdRequestHeaderName;
    }
    public void setReplayRequestIdRequestHeaderName(String replayRequestIdRequestHeaderName) {
        this.replayRequestIdRequestHeaderName = replayRequestIdRequestHeaderName;
    }
}
