package com.github.jerryxia.devhelper.requestcapture.support.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.UUID;

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

import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecord;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecordType;
import com.github.jerryxia.devhelper.requestcapture.support.RequestCaptureConstants;
import com.github.jerryxia.devhelper.util.SystemClock;
import com.github.jerryxia.devhelper.web.WebConstants;

/**
 * @author guqiankun
 *
 */
public class RequestCaptureFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(RequestCaptureFilter.class);

    public static final String PARAM_NAME_ENABLED                               = "enabled";
    public static final String PARAM_NAME_EXCLUSIONS                            = "exclusions";
    public static final String PARAM_NAME_REPLAY_REQUEST_ID_REQUEST_HEADER_NAME = "replayRequestIdRequestHeaderName";
    public static final String PARAM_NAME_MAX_PAYLOAD_LENGTH                    = "maxPayloadLength";

    private boolean         enabled;
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

        this.enabled = true;
        String enabledStr = filterConfig.getInitParameter(PARAM_NAME_ENABLED);
        this.enabled = Boolean.parseBoolean(enabledStr);

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

        RequestCaptureConstants.RECORD_MANAGER.init();
        WebConstants.REQUEST_CAPTURE_FILTER_ENABLED = true;

        log.debug("devhelper RequestCaptureFilter enabled                : true");
        log.debug("devhelper RequestCaptureFilter log_ext_enabled_status : " + RequestCaptureConstants.LOG_EXT_ENABLED);
        log.debug("devhelper RequestCaptureFilter log_ext_enabled_map    : "
                + RequestCaptureConstants.LOG_EXT_ENABLED_MAP);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (!enabled || isExclusion(httpRequest.getRequestURI())) {
            chain.doFilter(request, response);
        } else {
            Object requestCaptureIdObj = httpRequest.getAttribute(RequestCaptureConstants.REQUEST_CAPTURE_FILTER_NAME);
            if (requestCaptureIdObj == null) {
                // 首次filter
                HttpRequestRecord httpRequestRecord = buildHttpRequestRecord(httpRequest);
                httpRequest.setAttribute(RequestCaptureConstants.REQUEST_CAPTURE_FILTER_NAME,
                        httpRequestRecord.getId());
                RequestCaptureConstants.HTTP_REQUEST_RECORD_ID.set(httpRequestRecord.getId());
                RequestCaptureConstants.RECORD_MANAGER.allocEventProducer().publish(httpRequestRecord);

                // org.springframework.web.util.ContentCachingRequestWrapper
                ContentRecordRequestWrapper httpRequestWrapper = new ContentRecordRequestWrapper(httpRequest,
                        maxPayloadLength, null, new DefaultContentEndListener(httpRequestRecord));
                // org.springframework.web.util.ContentCachingResponseWrapper
                ContentRecordResponseWrapper httpResponseWrapper = null;
                if (response instanceof ContentRecordResponseWrapper) {
                    httpResponseWrapper = (ContentRecordResponseWrapper) response;
                } else {
                    httpResponseWrapper = new ContentRecordResponseWrapper((HttpServletResponse) response);
                }
                try {
                    chain.doFilter(httpRequestWrapper, httpResponseWrapper);
                } finally {
                    log.debug(new String(httpResponseWrapper.getContentAsByteArray()));
                    RequestCaptureConstants.HTTP_REQUEST_RECORD_ID.remove();
                    httpResponseWrapper.copyBodyToResponse();
                }
            } else {
                // forward include error
                RequestCaptureConstants.HTTP_REQUEST_RECORD_ID.set((String) requestCaptureIdObj);
                try {
                    chain.doFilter(request, response);
                } finally {
                    RequestCaptureConstants.HTTP_REQUEST_RECORD_ID.remove();
                }
            }
        }
    }

    @Override
    public void destroy() {
        RequestCaptureConstants.RECORD_MANAGER.shutdown();
    }

    private HttpRequestRecord buildHttpRequestRecord(HttpServletRequest httpRequest) {
        String id = null;
        if (WebConstants.REQUEST_ID_INIT_FILTER_ENABLED) {
            id = (String) httpRequest.getAttribute(WebConstants.REQUEST_ID_INIT_FILTER_NAME);
        } else {
            // 如果RequestIdInitFilter没有启用，则由RequestCaptureFilter生成Id
            id = UUID.randomUUID().toString();
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
        // org.apache.catalina.util.ParameterMap
        LinkedHashMap<String, String[]> parameterMap = new LinkedHashMap<String, String[]>(
                httpRequest.getParameterMap());
        LinkedHashMap<String, String[]> headers = new LinkedHashMap<String, String[]>();

        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String[] headerValues = toStringArray(httpRequest.getHeaders(headerName));
            headers.put(headerName, headerValues);
        }

        HttpRequestRecord httpRequestRecord = new HttpRequestRecord(id, type, timeStamp);
        httpRequestRecord.setMethod(method);
        httpRequestRecord.setRequestURL(requestURL);
        httpRequestRecord.setRequestURI(requestURI);
        httpRequestRecord.setQueryString(queryString);
        httpRequestRecord.setContentType(contentType);
        httpRequestRecord.setParameterMap(parameterMap);
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

    private String[] toStringArray(Enumeration<String> enumeration) {
        if (enumeration != null) {
            // List<String> lists = EnumerationUtils.toList(enumeration);
            final ArrayList<String> list = new ArrayList<String>(1);
            while (enumeration.hasMoreElements()) {
                list.add(enumeration.nextElement());
            }
            String[] arr = new String[list.size()];
            return list.toArray(arr);
        } else {
            return null;
        }
    }
}
