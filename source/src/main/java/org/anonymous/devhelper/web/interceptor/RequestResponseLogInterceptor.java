/**
 * 
 */
package org.anonymous.devhelper.web.interceptor;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import org.anonymous.devhelper.web.WebConstants;
import org.anonymous.devhelper.web.interceptor.RequestResponseLogInterceptor;

/**
 * 用于log请求、响应
 * 
 * @author guqk
 *
 */
public class RequestResponseLogInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLogInterceptor.class);

    // 只要加入了interceptors中默认启用
    private boolean enable = true;
    // 额外要记录的请求头
    private String[] logRequestHeaderNames = new String[0];

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (enable) {
            LinkedHashMap<String, String[]> map = new LinkedHashMap<String, String[]>();
            map.put("RequestURI", toStringArray(request.getRequestURI()));
            map.put("X-Call-RequestId", toStringArray(WebConstants.X_CALL_REQUEST_ID.get()));
            // 记录额外指定的请求头
            if (logRequestHeaderNames != null && logRequestHeaderNames.length > 0) {
                for (int i = 0; i < logRequestHeaderNames.length; i++) {
                    map.put(logRequestHeaderNames[i], toStringArray(request.getHeaders(logRequestHeaderNames[i])));
                }
            }

            Map<String, String[]> parameterMap = request.getParameterMap();
            if (parameterMap != null) {
                map.putAll(parameterMap);
            }
            dumpRequestMap(map);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (enable) {
            if (modelAndView != null) {
                // logger.info(JsonConvertor.serialize(modelAndView.getModelMap()));
                logger.info(modelAndView.getModelMap().toString());
            } else {
                logger.info("null");
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setLogRequestHeaderNames(String... logRequestHeaderNames) {
        this.logRequestHeaderNames = logRequestHeaderNames;
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
            //List<String> lists = EnumerationUtils.toList(enumeration);
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

    private void dumpRequestMap(LinkedHashMap<String, String[]> map) {
        Iterator<Entry<String, String[]>> i = map.entrySet().iterator();
        if (!i.hasNext()) {
            logger.info("{}");
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
            logger.info(sb.toString());
        }
    }

}
