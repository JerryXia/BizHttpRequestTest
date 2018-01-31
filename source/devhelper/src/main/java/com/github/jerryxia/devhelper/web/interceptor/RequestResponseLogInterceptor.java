/**
 * 
 */
package com.github.jerryxia.devhelper.web.interceptor;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServletServerHttpRequest;
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
    private final String lineSeparator;

    /**
     * 只要加入了interceptors中默认启用
     */
    private boolean enabled = true;

    /**
     * 额外要记录的请求头
     */
    private String[] logRequestHeaderNames = new String[0];

    public RequestResponseLogInterceptor() {
        String pLineSeparator = null;
        try {
            pLineSeparator = System.getProperty("line.separator");
        } catch (SecurityException se) {
            pLineSeparator = "\n";
        } finally {
            lineSeparator = pLineSeparator;
        }
    }

    public void init() {
        StringBuilder buf = new StringBuilder(logRequestHeaderNames.length * 16);
        for (int i = 0; i < logRequestHeaderNames.length; i++) {
            if (i > 0) {
                buf.append(',');
            }
            buf.append(logRequestHeaderNames[i]);
        }

        WebConstants.REQUEST_RESPONSE_LOG_INTERCEPTOR_ENABLED = true;
        log.debug("devhelper RequestResponseLogInterceptor enabled               : {}", enabled);
        log.debug("devhelper RequestResponseLogInterceptor logRequestHeaderNames : {}", buf.toString());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (this.enabled) {
            HttpSession session = request.getSession(false);
            Principal userPrincipal = request.getUserPrincipal();
            StringBuffer sb = new StringBuffer();
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
            appendKeyValueLine(sb, "headers", new ServletServerHttpRequest(request).getHeaders().toString());
            log.debug(sb.toString());

            // 记录额外指定的请求头
//            LinkedHashMap<String, String[]> map = new LinkedHashMap<String, String[]>();
//            if (logRequestHeaderNames != null && logRequestHeaderNames.length > 0) {
//                for (int i = 0; i < logRequestHeaderNames.length; i++) {
//                    map.put(logRequestHeaderNames[i], toStringArray(request.getHeaders(logRequestHeaderNames[i])));
//                }
//            }
//            Map<String, String[]> parameterMap = request.getParameterMap();
//            if (parameterMap != null) {
//                map.putAll(parameterMap);
//            }
//            dumpRequest(map);

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (this.enabled) {
            if (modelAndView != null) {
                log.debug(modelAndView.getModelMap().toString());
            } else {
                log.debug("ModelAndView returned: null");
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
//        System.err.println(request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));
//        System.err.println(request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE));
//        System.err.println(request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
        if(this.enabled && ex != null) {
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
        sb.append(key).append(": ").append(value).append(lineSeparator);
    }

    private String[] toStringArray(String item) {
        if (item != null) {
            String[] arr = new String[1];
            arr[0] = item;
            return arr;
        } else {
            return null;
        }
    }

    private String[] toStringArray(Enumeration<String> enumeration) {
        if (enumeration != null) {
            // List<String> lists = EnumerationUtils.toList(enumeration);
            final ArrayList<String> list = new ArrayList<String>(16);
            while (enumeration.hasMoreElements()) {
                list.add(enumeration.nextElement());
            }
            String[] arr = new String[list.size()];
            return list.toArray(arr);
        } else {
            return null;
        }
    }

    private void dumpRequest(LinkedHashMap<String, String[]> map) {
        Iterator<Entry<String, String[]>> i = map.entrySet().iterator();
        if (!i.hasNext()) {
            log.debug("{}");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append('{');
            for (;;) {
                Entry<String, String[]> e = i.next();
                String key = e.getKey();
                String[] value = e.getValue();
                sb.append(key);
                sb.append('=');

                if (value != null) {
                    if (value.length > 1) {
                        sb.append('[');
                        for (int valueIndex = 0; valueIndex < value.length; valueIndex++) {
                            sb.append(value[valueIndex]);
                            if (valueIndex < value.length - 1) {
                                sb.append(',').append(' ');
                            }
                        }
                        sb.append("]");
                    } else if (value.length == 1) {
                        sb.append(value[0]);
                    } else {
                        // ''
                    }
                } else {
                    // ''
                }

                if (!i.hasNext()) {
                    sb.append('}');
                    break;
                } else {
                    sb.append(',').append(' ');
                }
            }
            log.debug(sb.toString());
        }
    }

}
