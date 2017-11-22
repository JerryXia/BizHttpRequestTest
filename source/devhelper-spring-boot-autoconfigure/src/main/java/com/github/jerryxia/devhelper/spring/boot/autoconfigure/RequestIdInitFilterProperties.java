/**
 * 
 */
package com.github.jerryxia.devhelper.spring.boot.autoconfigure;

/**
 * @author Administrator
 *
 */
public class RequestIdInitFilterProperties {
    private String requestIdResponseHeaderName;

    public String getRequestIdResponseHeaderName() {
        return requestIdResponseHeaderName;
    }

    public void setRequestIdResponseHeaderName(String requestIdResponseHeaderName) {
        this.requestIdResponseHeaderName = requestIdResponseHeaderName;
    }

}
