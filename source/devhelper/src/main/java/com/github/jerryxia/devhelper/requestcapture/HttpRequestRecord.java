package com.github.jerryxia.devhelper.requestcapture;

import java.util.Map;

/**
 * @author Administrator
 *
 */
public class HttpRequestRecord {

    private final String id;
    private final HttpRequestRecordType type;
    private final long timeStamp;
    private String method;
    private String requestURL;
    private String requestURI;
    private String queryString;
    private String contentType;
    private Map<String, String[]> parameterMap;
    private String payload;
    private Map<String, String[]> headers;

    public HttpRequestRecord(String uuid, HttpRequestRecordType type, long timeStamp) {
        switch (type) {
        case NORMAL:
        case REPLAY:
            this.id = uuid;
            this.type = type;
            this.timeStamp = timeStamp;
            break;
        case UNKNOWN:
        default:
            throw new IllegalArgumentException("Invalid HttpRequestType");
        }
    }

    public String getId() {
        return id;
    }

    public HttpRequestRecordType getType() {
        return type;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Map<String, String[]> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String[]> headers) {
        this.headers = headers;
    }
}
