package com.github.jerryxia.devhelper.requestcapture.support.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jerryxia.devhelper.Constants;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecord;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecordType;
import com.github.jerryxia.devhelper.requestcapture.support.RequestCaptureConstants;
import com.github.jerryxia.devhelper.util.ServletUtil;
import com.github.jerryxia.devhelper.web.WebConstants;
import com.github.jerryxia.devutil.ObjectId;
import com.github.jerryxia.devutil.SystemClock;

/**
 * <b> reference code: </b>
 * <p>
 * https://github.com/vy/hrrs/blob/master/servlet-filter/src/main/java/com/vlkan/hrrs/servlet/HrrsUrlEncodedFormHelper.java
 * </p>
 * 
 * @author guqiankun
 *
 */
public class RequestCaptureFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(RequestCaptureFilter.class);

    public static final String PARAM_NAME_ENABLED                               = "enabled";
    public static final String PARAM_NAME_EXCLUSIONS                            = "exclusions";
    public static final String PARAM_NAME_REPLAY_REQUEST_ID_REQUEST_HEADER_NAME = "replayRequestIdRequestHeaderName";
    public static final String PARAM_NAME_MAX_PAYLOAD_LENGTH                    = "maxPayloadLength";

    private boolean         enabled = true;
    private String          contextPath;
    private HashSet<String> excludesPattern;
    private String          replayRequestIdRequestHeaderName;
    private int             maxPayloadLength;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.contextPath = filterConfig.getServletContext().getContextPath();
        if (this.contextPath == null || this.contextPath.length() == 0) {
            this.contextPath = "/";
        }

        this.enabled = Boolean.parseBoolean(filterConfig.getInitParameter(PARAM_NAME_ENABLED));
        RequestCaptureConstants.REQUEST_CAPTURE_FILTER_ENABLED = this.enabled;

        String exclusions = filterConfig.getInitParameter(PARAM_NAME_EXCLUSIONS);
        if (exclusions != null && exclusions.trim().length() != 0) {
            this.excludesPattern = new HashSet<String>(Arrays.asList(exclusions.split("\\s*,\\s*")));
        }

        String cfgRequestHeaderName = filterConfig.getInitParameter(PARAM_NAME_REPLAY_REQUEST_ID_REQUEST_HEADER_NAME);
        if (cfgRequestHeaderName != null && cfgRequestHeaderName.trim().length() > 0) {
            replayRequestIdRequestHeaderName = cfgRequestHeaderName;
        } else {
            replayRequestIdRequestHeaderName = WebConstants.REPLAY_HTTP_REQUEST_HEADER_NAME;
        }

        String maxPayloadLengthStr = filterConfig.getInitParameter(PARAM_NAME_MAX_PAYLOAD_LENGTH);
        if (maxPayloadLengthStr != null && maxPayloadLengthStr.trim().length() > 0) {
            this.maxPayloadLength = Integer.parseInt(maxPayloadLengthStr);
        } else {
            this.maxPayloadLength = RequestCaptureConstants.DEFAULT_PAYLOAD_LENGTH;
        }

        filterConfig.getServletContext().log("devhelper RequestCaptureFilter enabled                : "
                + RequestCaptureConstants.REQUEST_CAPTURE_FILTER_ENABLED);
        filterConfig.getServletContext().log(
                "devhelper RequestCaptureFilter log_ext_enabled_status : " + RequestCaptureConstants.LOG_EXT_ENABLED);
        filterConfig.getServletContext().log("devhelper RequestCaptureFilter log_ext_enabled_map    : "
                + RequestCaptureConstants.LOG_EXT_ENABLED_MAP.toString());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (!enabled || isExclusion(httpRequest.getRequestURI())) {
            chain.doFilter(request, response);
        } else {
            switch (httpRequest.getDispatcherType()) {
            case REQUEST:
                dispatchRequest(httpRequest, response, chain);
                break;
            case FORWARD:
                dispatchForward(httpRequest, response, chain);
                break;
            case INCLUDE:
                dispatchInclude(httpRequest, response, chain);
                break;
            case ERROR:
                dispatchError(httpRequest, response, chain);
                break;
            case ASYNC:
            default:
                chain.doFilter(request, response);
                break;
            }
        }
    }

    @Override
    public void destroy() {
        Constants.EVENT_WORKING_GROUP.shutdown();
    }

    private void dispatchRequest(HttpServletRequest httpRequest, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 首次filter
        HttpRequestRecord httpRequestRecord = buildHttpRequestRecord(httpRequest);
        // httpRequestRecord.getId() --> RequestCaptureId
        httpRequest.setAttribute(RequestCaptureConstants.REQUEST_CAPTURE_FILTER_ID, httpRequestRecord.getId());
        // 存到当前线程
        RequestCaptureConstants.HTTP_REQUEST_RECORD_ID.set(httpRequestRecord.getId());

        Constants.EVENT_WORKING_GROUP.allocEventProducer().publish(httpRequestRecord);

        // org.springframework.web.util.ContentCachingRequestWrapper
        ContentRecordRequestWrapper httpRequestWrapper = null;
        if (httpRequest instanceof ContentRecordRequestWrapper) {
            httpRequestWrapper = (ContentRecordRequestWrapper) httpRequest;
        } else {
            httpRequestWrapper = new ContentRecordRequestWrapper(httpRequest, maxPayloadLength, null,
                    new DefaultContentEndListener(httpRequestRecord));
        }

        // org.springframework.web.util.ContentCachingResponseWrapper
        ContentRecordResponseWrapper httpResponseWrapper = null;
        if (response instanceof ContentRecordResponseWrapper) {
            httpResponseWrapper = (ContentRecordResponseWrapper) response;
        } else {
            httpResponseWrapper = new ContentRecordResponseWrapper((HttpServletResponse) response);
        }

        try {
            if (log.isTraceEnabled()) {
                log.trace("dispatchRequest pre doFilter");
                chain.doFilter(httpRequestWrapper, httpResponseWrapper);
                log.trace("dispatchRequest post doFilter");
            } else {
                chain.doFilter(httpRequestWrapper, httpResponseWrapper);
            }
        } finally {
            if (log.isTraceEnabled()) {
                log.trace("dispatchRequest finally");
            }
            if (log.isDebugEnabled()) {
                String responseBody = new String(httpResponseWrapper.getContentAsByteArray());
                log.debug(responseBody);
            }
            RequestCaptureConstants.HTTP_REQUEST_RECORD_ID.remove();
            httpResponseWrapper.copyBodyToResponse();
        }
    }

    private void dispatchForward(HttpServletRequest httpRequest, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Object requestCaptureIdObj = httpRequest.getAttribute(RequestCaptureConstants.REQUEST_CAPTURE_FILTER_ID);
        if (requestCaptureIdObj == null) {
            // 之前的请求isExclusion = true, 那么本次forward的请求也忽略
        } else {
            // 其实可以不用再次赋值
            RequestCaptureConstants.HTTP_REQUEST_RECORD_ID.set((String) requestCaptureIdObj);
        }
        if (log.isTraceEnabled()) {
            log.trace(" - dispatchForward pre doFilter");
            chain.doFilter(httpRequest, response);
            log.trace(" - dispatchForward post doFilter");
        } else {
            chain.doFilter(httpRequest, response);
        }
    }

    private void dispatchInclude(HttpServletRequest httpRequest, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Object requestCaptureIdObj = httpRequest.getAttribute(RequestCaptureConstants.REQUEST_CAPTURE_FILTER_ID);
        if (requestCaptureIdObj == null) {
            // 之前的请求isExclusion = true, 那么本次Include的请求也忽略
        } else {
            // 其实可以不用再次赋值
            RequestCaptureConstants.HTTP_REQUEST_RECORD_ID.set((String) requestCaptureIdObj);
        }
        if (log.isTraceEnabled()) {
            log.trace(" - dispatchInclude pre doFilter");
            chain.doFilter(httpRequest, response);
            log.trace(" - dispatchInclude post doFilter");
        } else {
            chain.doFilter(httpRequest, response);
        }
    }

    private void dispatchError(HttpServletRequest httpRequest, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Object requestCaptureIdObj = httpRequest.getAttribute(RequestCaptureConstants.REQUEST_CAPTURE_FILTER_ID);
        if (requestCaptureIdObj == null) {
            // 之前的请求isExclusion = true, 那么本次forward的请求也忽略
            chain.doFilter(httpRequest, response);
        } else {
            // error之前的请求已经进入filter中finally块执行HTTP_REQUEST_RECORD_ID.remove()
            RequestCaptureConstants.HTTP_REQUEST_RECORD_ID.set((String) requestCaptureIdObj);

            ContentRecordResponseWrapper httpResponseWrapper = null;
            if (response instanceof ContentRecordResponseWrapper) {
                httpResponseWrapper = (ContentRecordResponseWrapper) response;
            } else {
                httpResponseWrapper = new ContentRecordResponseWrapper((HttpServletResponse) response);
            }

            try {
                if (log.isTraceEnabled()) {
                    log.trace("dispatchError pre doFilter");
                    chain.doFilter(httpRequest, httpResponseWrapper);
                    log.trace("dispatchError post doFilter");
                } else {
                    chain.doFilter(httpRequest, httpResponseWrapper);
                }
            } finally {
                if (log.isTraceEnabled()) {
                    log.trace("dispatchError finally");
                }
                if (log.isErrorEnabled()) {
                    String responseBody = new String(httpResponseWrapper.getContentAsByteArray());
                    log.error(responseBody);
                }
                RequestCaptureConstants.HTTP_REQUEST_RECORD_ID.remove();
                httpResponseWrapper.copyBodyToResponse();
            }
        }
    }

    private HttpRequestRecord buildHttpRequestRecord(HttpServletRequest httpRequest) {
        String id = null;
        if (WebConstants.REQUEST_ID_INIT_FILTER_ENABLED) {
            id = (String) httpRequest.getAttribute(WebConstants.REQUEST_ID_INIT_FILTER_ID);
        } else {
            // 如果RequestIdInitFilter没有启用，则由RequestCaptureFilter生成Id
            id = ObjectId.get().toString();
        }

        long timeStamp = SystemClock.now();

        HttpRequestRecordType type = HttpRequestRecordType.UNKNOWN;
        String replayReqId = httpRequest.getHeader(replayRequestIdRequestHeaderName);
        if (replayReqId != null && replayReqId.length() > 0) {
            type = HttpRequestRecordType.REPLAY;
        } else {
            type = HttpRequestRecordType.NORMAL;
        }

        String method = httpRequest.getMethod();
        String requestURI = httpRequest.getRequestURI();
        String requestURL = httpRequest.getRequestURL().toString();
        String queryString = httpRequest.getQueryString();
        String contentType = httpRequest.getContentType();
        // deepClone org.apache.catalina.util.ParameterMap
        String encoding = httpRequest.getCharacterEncoding();
        if (encoding == null) {
            encoding = "UTF-8";
        }
        LinkedHashMap<String, String[]> formParameters = ServletUtil.parseFormParameters(httpRequest, encoding);
        LinkedHashMap<String, String[]> headers = ServletUtil.parseHeaders(httpRequest);

        HttpRequestRecord httpRequestRecord = new HttpRequestRecord(id, type, timeStamp);
        httpRequestRecord.setMethod(method);
        httpRequestRecord.setRequestURL(requestURL);
        httpRequestRecord.setRequestURI(requestURI);
        httpRequestRecord.setQueryString(queryString);
        httpRequestRecord.setContentType(contentType);
        httpRequestRecord.setParameterMap(formParameters);
        httpRequestRecord.setHeaders(headers);
        return httpRequestRecord;
    }

    private boolean isExclusion(String requestURI) {
        if (excludesPattern == null || requestURI == null) {
            return false;
        }

        if (contextPath != null && requestURI.startsWith(contextPath)) {
            requestURI = requestURI.substring(contextPath.length());
            if (!requestURI.startsWith("/")) {
                requestURI = "/" + requestURI;
            }
        }

        for (String pattern : excludesPattern) {
            if (pathMatches(pattern, requestURI)) {
                return true;
            }
        }

        return false;
    }

    /**
     * <p>
     * three type: endsWithMatch(eg. /xxx*=/xxx/xyz), startsWithMatch(eg.*.xxx=abc.xxx), equals(eg. /xxx=/xxx).
     * </p>
     * <b>Notice</b>: *xxx* will match *xxxyyyy. endsWithMatch first.
     */
    private boolean pathMatches(String pattern, String source) {
        if (pattern == null || source == null) {
            return false;
        }
        pattern = pattern.trim();
        source = source.trim();
        if (pattern.endsWith("*")) {
            // pattern: /druid* source:/druid/index.html
            int length = pattern.length() - 1;
            if (source.length() >= length) {
                if (pattern.substring(0, length).equals(source.substring(0, length))) {
                    return true;
                }
            }
        } else if (pattern.startsWith("*")) {
            // pattern: *.html source:/xx/xx.html
            int length = pattern.length() - 1;
            if (source.length() >= length && source.endsWith(pattern.substring(1))) {
                return true;
            }
        } else if (pattern.contains("*")) {
            // pattern: /druid/*/index.html source:/druid/admin/index.html
            int start = pattern.indexOf("*");
            int end = pattern.lastIndexOf("*");
            if (source.startsWith(pattern.substring(0, start)) && source.endsWith(pattern.substring(end + 1))) {
                return true;
            }
        } else {
            // pattern: /druid/index.html source:/druid/index.html
            if (pattern.equals(source)) {
                return true;
            }
        }
        return false;
    }

}
