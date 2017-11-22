/**
 * 
 */
package com.github.jerryxia.devhelper.spring.boot.autoconfigure;

/**
 * @author Administrator
 *
 */
public class RequestCaptureWebServletProperties {
    private String urlPattern;

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }
}
