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
    private RequestIdInitFilterProperties      requestIdInit         = new RequestIdInitFilterProperties();
    private RequestCaptureFilterProperties     requestCapture        = new RequestCaptureFilterProperties();
    private RequestCaptureWebServletProperties requestCaptureServlet = new RequestCaptureWebServletProperties();
    // private SnoopServletProperties snoopServlet = new SnoopServletProperties();
    private RequestResponseLogProperties requestResponseLog = new RequestResponseLogProperties();
    private ElmahServletProperties       elmahServlet       = new ElmahServletProperties();
    private TaskRunProperties            taskRun            = new TaskRunProperties();

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

    public RequestResponseLogProperties getRequestResponseLog() {
        return requestResponseLog;
    }

    public void setRequestResponseLog(RequestResponseLogProperties requestResponseLog) {
        this.requestResponseLog = requestResponseLog;
    }

    public ElmahServletProperties getElmahServlet() {
        return elmahServlet;
    }

    public void setElmahServlet(ElmahServletProperties elmahServlet) {
        this.elmahServlet = elmahServlet;
    }


    public TaskRunProperties getTaskRun() {
        return taskRun;
    }


    public void setTaskRun(TaskRunProperties taskRun) {
        this.taskRun = taskRun;
    }

}
