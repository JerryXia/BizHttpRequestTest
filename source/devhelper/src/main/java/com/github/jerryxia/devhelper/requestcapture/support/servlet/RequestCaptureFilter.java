package com.github.jerryxia.devhelper.requestcapture.support.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecord;
import com.github.jerryxia.devhelper.requestcapture.HttpRequestRecordType;
import com.github.jerryxia.devhelper.requestcapture.support.RequestCaptureConstants;
import com.github.jerryxia.devhelper.util.SystemClock;
import com.github.jerryxia.devhelper.web.WebConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guqiankun
 *
 */
public class RequestCaptureFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RequestCaptureFilter.class);

    public static final String PARAM_NAME_EXCLUSIONS                            = "exclusions";
    public static final String PARAM_NAME_REPLAY_REQUEST_ID_REQUEST_HEADER_NAME = "replayRequestIdRequestHeaderName";

    private String      contextPath;
    private Set<String> excludesPattern;
    private String      replayRequestIdRequestHeaderName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.contextPath = filterConfig.getServletContext().getContextPath();
        if (this.contextPath == null || this.contextPath.length() == 0) {
            this.contextPath = "/";
        }

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

        logger.info("requestcapture log_ext_enabled_status: {}", RequestCaptureConstants.LOG_EXT_ENABLED_STATUS);
        logger.info("requestcapture log_ext_enabled_map: {}", RequestCaptureConstants.LOG_EXT_ENABLED_MAP);
        RequestCaptureConstants.RECORD_MANAGER.init();
        WebConstants.REQUEST_CAPTURE_FILTER_ENABLED = true;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (isExclusion(httpRequest.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        HttpRequestRecord httpRequestRecord = buildHttpRequestRecord(httpRequest);
        RequestCaptureConstants.HTTP_REQUEST_RECORD_ID.set(httpRequestRecord.getId());
        RequestCaptureConstants.RECORD_MANAGER.allocEventProducer().publish(httpRequestRecord);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        RequestCaptureConstants.RECORD_MANAGER.shutdown();
    }

    private HttpRequestRecord buildHttpRequestRecord(HttpServletRequest httpRequest) {
        String id = null;
        if (WebConstants.REQUEST_ID_INIT_FILTER_ENABLED) {
            id = WebConstants.X_CALL_REQUEST_ID.get();
        } else {
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
        String accept = httpRequest.getHeader("accept");
        String contentType = httpRequest.getContentType();
        Map<String, String[]> parameterMap = new HashMap<String, String[]>(httpRequest.getParameterMap());

        HttpRequestRecord httpRequestRecord = new HttpRequestRecord(id, type, timeStamp);
        httpRequestRecord.setMethod(method);
        httpRequestRecord.setRequestURL(requestURL);
        httpRequestRecord.setRequestURI(requestURI);
        httpRequestRecord.setQueryString(queryString);
        httpRequestRecord.setAccept(accept);
        httpRequestRecord.setContentType(contentType);
        httpRequestRecord.setParameterMap(parameterMap);
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
