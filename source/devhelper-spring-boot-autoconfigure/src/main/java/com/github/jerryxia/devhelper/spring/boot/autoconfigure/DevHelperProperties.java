/**
 * 
 */
package com.github.jerryxia.devhelper.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author guqk
 *
 */
@ConfigurationProperties(prefix = "devhelper")
public class DevHelperProperties {
    private RequestIdInitFilterProperties requestIdInit = new RequestIdInitFilterProperties();
    private RequestCaptureFilterProperties requestCapture = new RequestCaptureFilterProperties();
    private RequestCaptureWebServletProperties requestCaptureServlet = new RequestCaptureWebServletProperties();
    private SnoopServletProperties snoopServlet = new SnoopServletProperties();

    public RequestIdInitFilterProperties getRequestIdInit() {
        return requestIdInit;
    }

    public void setRequestIdInit(RequestIdInitFilterProperties requestIdInit) {
        this.requestIdInit = requestIdInit;
    }

    public RequestCaptureFilterProperties getRequestCapture() {
        return requestCapture;
    }

    public void setRequestCapture(RequestCaptureFilterProperties requestCapture) {
        this.requestCapture = requestCapture;
    }

    public RequestCaptureWebServletProperties getRequestCaptureServlet() {
        return requestCaptureServlet;
    }

    public void setRequestCaptureServlet(RequestCaptureWebServletProperties requestCaptureServlet) {
        this.requestCaptureServlet = requestCaptureServlet;
    }

    public SnoopServletProperties getSnoopServlet() {
        return snoopServlet;
    }

    public void setSnoopServlet(SnoopServletProperties snoopServlet) {
        this.snoopServlet = snoopServlet;
    }
}
