package com.github.jerryxia.devhelper.requestcapture.support.servlet;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecord;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecordType;

/**
 * @author guqiankun
 *
 */
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
        String replayHeaderReqId = httpRequest.getHeader(REPLAYHTTPREQUEST_HEADERNAME);
        if (replayHeaderReqId != null && replayHeaderReqId.length() > 0) {
            httpRequestRecord = new HttpRequestRecord(HttpRequestRecordType.REPLAY, replayHeaderReqId);
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
