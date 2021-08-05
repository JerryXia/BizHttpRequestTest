/**
 * 
 */
package com.github.jerryxia.devhelper.spring.boot.autoconfigure;

/**
 * @author Administrator
 *
 */
public class RequestCaptureFilterProperties {
    public static final String DEFAULT_STATIC_RESOURCE_EXCLUSIONS = "*.js,*.gif,*.jpg,*.png,*.css,*.ico";

    private Boolean enabled;
    private String  exclusions;
    private String  replayRequestIdRequestHeaderName;
    private Integer maxPayloadLength;

    public Boolean getEnabled() {
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

    public Integer getMaxPayloadLength() {
        return maxPayloadLength;
    }

    public void setMaxPayloadLength(Integer maxPayloadLength) {
        this.maxPayloadLength = maxPayloadLength;
    }
}
