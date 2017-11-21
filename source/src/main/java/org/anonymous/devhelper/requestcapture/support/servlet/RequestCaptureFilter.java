package org.anonymous.devhelper.requestcapture.support.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.anonymous.devhelper.requestcapture.HttpRequestRecord;
import org.anonymous.devhelper.requestcapture.support.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guqiankun
 *
 */
public class RequestCaptureFilter extends AbstractRequestCaptureImpl implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RequestCaptureFilter.class);

    private static final String PARAM_NAME_EXCLUSIONS = "exclusions";
    private String              contextPath;
    private Set<String>         excludesPattern;

    public RequestCaptureFilter() {
        // ctor
    }

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
        logger.info("org.anonymous.devhelper.requestcapture.support.servlet.RequestCaptureFilter init");
        logger.info("requestcapture log_ext_enabled_status: {}", Constants.LOG_EXT_ENABLED_STATUS);
        logger.info("requestcapture log_ext_enabled_map: {}", Constants.LOG_EXT_ENABLED_MAP);
        Constants.RECORD_MANAGER.init();
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
        if (Constants.LOG_EXT_ENABLED_STATUS) {
            Constants.HTTP_REQUEST_RECORD_ID.set(httpRequestRecord.getId());
            Constants.HTTP_REQUEST_RECORD_REPLAYING_REQUEST_ID.set(httpRequestRecord.getReplayingRequestId());
        }
        Constants.RECORD_MANAGER.allocEventProducer().publish(httpRequestRecord);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Constants.RECORD_MANAGER.shutdown();
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
    public boolean pathMatches(String pattern, String source) {
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
