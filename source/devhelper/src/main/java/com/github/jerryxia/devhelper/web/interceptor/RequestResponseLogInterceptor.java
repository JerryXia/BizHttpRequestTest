/**
 * 
 */
package com.github.jerryxia.devhelper.web.interceptor;

import java.security.Principal;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.github.jerryxia.devhelper.web.WebConstants;

/**
 * 用于log请求、响应
 * 
 * @author guqk
 *
 */
public class RequestResponseLogInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(RequestResponseLogInterceptor.class);

    /**
     * 只要加入了interceptors中默认启用
     */
    private boolean enabled = true;

    /**
     * 额外要记录的请求头
     */
    private String[] logRequestHeaderNames = new String[0];

    public void init() {
        StringBuilder buf = new StringBuilder(logRequestHeaderNames.length * 16);
        for (int i = 0; i < logRequestHeaderNames.length; i++) {
            if (i > 0) {
                buf.append(',');
            }
            buf.append(logRequestHeaderNames[i]);
        }

        WebConstants.REQUEST_RESPONSE_LOG_INTERCEPTOR_ENABLED = enabled;
        log.debug("devhelper RequestResponseLogInterceptor enabled               : {}", enabled);
        log.debug("devhelper RequestResponseLogInterceptor logRequestHeaderNames : {}", buf.toString());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (this.enabled) {
            HttpSession session = request.getSession(false);
            Principal userPrincipal = request.getUserPrincipal();
            // TODO: 默认大小支持配置
            StringBuffer sb = new StringBuffer(512);
            appendKeyValueLine(sb, "requestURI", request.getRequestURI());
            appendKeyValueLine(sb, "contextPath", request.getContextPath());
            appendKeyValueLine(sb, "pathInfo", request.getPathInfo());
            appendKeyValueLine(sb, "pathTranslated", request.getPathTranslated());
            appendKeyValueLine(sb, "requestedSessionId", request.getRequestedSessionId());
            appendKeyValueLine(sb, "session_id", session == null ? null : session.getId());
            appendKeyValueLine(sb, "remoteAddress", request.getRemoteAddr());
            appendKeyValueLine(sb, "authType", request.getAuthType());
            appendKeyValueLine(sb, "remoteUser", request.getRemoteUser());
            appendKeyValueLine(sb, "userPrincipal", userPrincipal == null ? null : userPrincipal.getName());
            // 记录额外指定的请求头
            appendKeyValueLine(sb, "logRequestHeaderNames", "");
            if (logRequestHeaderNames != null && logRequestHeaderNames.length > 0) {
                for (int i = 0; i < logRequestHeaderNames.length; i++) {
                    Enumeration<String> enumeration = request.getHeaders(logRequestHeaderNames[i]);
                    while (enumeration.hasMoreElements()) {
                        appendSeperateKeyValueLine(sb, logRequestHeaderNames[i], enumeration.nextElement());
                    }
                }
            }
            if (log.isDebugEnabled()) {
                log.debug(sb.toString());
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor
        // org.springframework.web.servlet.ModelAndView
        if (this.enabled) {
            if (modelAndView == null) {
                if (log.isDebugEnabled()) {
                    log.debug("ModelAndView returned: null");
                }
            } else {
                StringBuffer sb = new StringBuffer(256);
                appendKeyValueLine(sb, "ModelAndView returned", "");
                if (modelAndView.getStatus() == null) {
                    appendSeperateKeyValueLine(sb, "status", null);
                } else {
                    appendSeperateKeyValueLine(sb, "status", modelAndView.getStatus().toString(),
                            modelAndView.getStatus().getReasonPhrase());
                }
                appendSeperateKeyValueLine(sb, "view", modelAndView.getViewName());
                appendSeperateKeyValueLine(sb, "model", modelAndView.getModelMap().toString());
                if (log.isDebugEnabled()) {
                    log.debug(sb.toString());
                }
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // System.err.println(request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));
        // System.err.println(request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE));
        // System.err.println(request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
        if (this.enabled && ex != null && log.isErrorEnabled()) {
            log.error(handler.toString(), ex);
        }
    }

    public void setEnabled(boolean enable) {
        this.enabled = enable;
    }

    public void setLogRequestHeaderNames(String... logRequestHeaderNames) {
        this.logRequestHeaderNames = logRequestHeaderNames;
    }

    private void appendKeyValueLine(StringBuffer sb, String key, String value) {
        // {key}: {value}\n
        sb.append(key).append(':').append(' ').append(value).append(System.lineSeparator());
    }

    private void appendSeperateKeyValueLine(StringBuffer sb, String key, String value) {
        // - {key}: {value}\n
        sb.append(' ').append('-').append(' ').append(key).append(':').append(' ').append(value)
                .append(System.lineSeparator());
    }

    private void appendSeperateKeyValueLine(StringBuffer sb, String key, String v1, String v2) {
        // - {key}: {v1} {v2}\n
        sb.append(' ').append('-').append(' ').append(key).append(':').append(' ').append(v1).append(' ').append(v2)
                .append(System.lineSeparator());
    }
}
