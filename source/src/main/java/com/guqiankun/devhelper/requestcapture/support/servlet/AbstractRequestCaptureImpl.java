package com.guqiankun.devhelper.requestcapture.support.servlet;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.guqiankun.devhelper.requestcapture.HttpRequestRecord;
import com.guqiankun.devhelper.requestcapture.HttpRequestRecordType;

public abstract class AbstractRequestCaptureImpl {

    protected static final String REPLAYHTTPREQUEST_HEADERNAME = "X-ReplayHttpRequest-Id";

    protected HttpRequestRecord buildHttpRequestRecord(HttpServletRequest httpRequest) {
        String method = httpRequest.getMethod();
        String requestURI = httpRequest.getRequestURI();
        String requestURL = httpRequest.getRequestURL().toString();
        String queryString = httpRequest.getQueryString();
        String accept = httpRequest.getHeader("accept");
        String contentType = httpRequest.getContentType();
        Map<String, String[]> parameterMap = new HashMap<String, String[]>(httpRequest.getParameterMap());

        HttpRequestRecord httpRequestRecord = null;
        String replayHeader_ReqId = httpRequest.getHeader(REPLAYHTTPREQUEST_HEADERNAME);
        if (replayHeader_ReqId != null && replayHeader_ReqId.length() > 0) {
            httpRequestRecord = new HttpRequestRecord(HttpRequestRecordType.REPLAY, replayHeader_ReqId);
        } else {
            httpRequestRecord = new HttpRequestRecord(HttpRequestRecordType.NORMAL, null);
        }
        httpRequestRecord.setMethod(method);
        httpRequestRecord.setRequestURL(requestURL);
        httpRequestRecord.setRequestURI(requestURI);
        httpRequestRecord.setQueryString(queryString);
        httpRequestRecord.setAccept(accept);
        httpRequestRecord.setContentType(contentType);
        httpRequestRecord.setParameterMap(parameterMap);
        return httpRequestRecord;
    }

}
