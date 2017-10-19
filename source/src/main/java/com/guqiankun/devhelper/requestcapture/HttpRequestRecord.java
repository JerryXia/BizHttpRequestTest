package com.guqiankun.devhelper.requestcapture;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.guqiankun.devhelper.requestcapture.Assert;
import com.guqiankun.devhelper.requestcapture.ObjectId;

public class HttpRequestRecord {

    private String                  id;
    private String                  replayingRequestId;
    private HttpRequestRecordType   type;
    private List<HttpRequestRecord> replayedHttpRequestRecords;

    private String                method;
    private String                requestURL;
    private String                requestURI;
    private String                queryString;
    private String                accept;
    private String                contentType;
    private Map<String, String[]> parameterMap;

    public HttpRequestRecord(HttpRequestRecordType type, String replayingRequestId) {
        this.id = ObjectId.get().toString();
        switch (type) {
        case NORMAL:
            this.type = HttpRequestRecordType.NORMAL;
            this.replayingRequestId = UUID.randomUUID().toString();
            this.replayedHttpRequestRecords = new LinkedList<HttpRequestRecord>();
            break;
        case REPLAY:
            Assert.hasLength(replayingRequestId);
            this.type = HttpRequestRecordType.REPLAY;
            this.replayingRequestId = replayingRequestId;
            break;
        case UNKNOWN:
        default:
            throw new IllegalArgumentException("Invalid HttpRequestType");
        }
    }

    public String getId() {
        return id;
    }

    public String getReplayingRequestId() {
        return replayingRequestId;
    }

    public HttpRequestRecordType getType() {
        return type;
    }

    public List<HttpRequestRecord> getReplayedHttpRequestRecords() {
        return replayedHttpRequestRecords;
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

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
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

}