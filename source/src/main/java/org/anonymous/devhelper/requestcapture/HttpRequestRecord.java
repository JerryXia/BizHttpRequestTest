package org.anonymous.devhelper.requestcapture;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.anonymous.devhelper.util.Assert;
import org.anonymous.devhelper.util.ObjectId;

/**
 * @author Administrator
 *
 */
public class HttpRequestRecord {

    private final String                id;
    private final HttpRequestRecordType type;
    private final String                replayingRequestId;
    private List<HttpRequestRecord>     replayedHttpRequestRecords;
    private String                      method;
    private String                      requestURL;
    private String                      requestURI;
    private String                      queryString;
    private String                      accept;
    private String                      contentType;
    private Map<String, String[]>       parameterMap;

    public HttpRequestRecord(HttpRequestRecordType type, String replayingRequestId) {
        this.id = ObjectId.get().toString();
        switch (type) {
        case NORMAL:
            this.type = type;
            //this.replayingRequestId = UUID.randomUUID().toString();
            this.replayingRequestId = replayingRequestId;
            this.replayedHttpRequestRecords = new LinkedList<HttpRequestRecord>();
            break;
        case REPLAY:
            Assert.hasLength(replayingRequestId);
            this.type = type;
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
