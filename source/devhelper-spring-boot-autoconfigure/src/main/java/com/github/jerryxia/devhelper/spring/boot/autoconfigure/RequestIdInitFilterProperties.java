/**
 *
 */
package com.github.jerryxia.devhelper.spring.boot.autoconfigure;

/**
 * @author Administrator
 *
 */
public class RequestIdInitFilterProperties {
    public static final String FILTERED_ALL_URL_PATTERN = "/*";

    private boolean enabled;
    private String requestIdResponseHeaderName;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRequestIdResponseHeaderName() {
        return requestIdResponseHeaderName;
    }

    public void setRequestIdResponseHeaderName(String requestIdResponseHeaderName) {
        this.requestIdResponseHeaderName = requestIdResponseHeaderName;
    }

}
