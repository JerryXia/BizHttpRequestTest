/**
 * 
 */
package com.github.jerryxia.devhelper.web.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.jerryxia.devhelper.web.WebConstants;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 用于为每次请求初始化一个唯一id
 * 
 * @author guqk
 *
 */
public class RequestIdInitInterceptor extends HandlerInterceptorAdapter {

    /**
     * response header name defaultValue: X-Call-RequestId
     */
    private String requestIdResponseHeaderName = WebConstants.REQUEST_ID_RESPONSE_HEADER_NAME;

    public void init() {
        WebConstants.REQUEST_ID_INIT_INTERCEPTOR_ENABLED = true;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 前置过滤器没有被配置使用, 就在这个拦截器重新初始化一次
        if (WebConstants.REQUEST_ID_INIT_FILTER_ENABLED == false) {
            String requestId = UUID.randomUUID().toString();
            WebConstants.X_CALL_REQUEST_ID.set(requestId);
            response.setHeader(requestIdResponseHeaderName, requestId);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        //
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 前置过滤器没有被配置使用, 就在这个拦截器结束掉
        if (WebConstants.REQUEST_ID_INIT_FILTER_ENABLED == false) {
            WebConstants.X_CALL_REQUEST_ID.remove();
        }
    }

    public String getRequestIdResponseHeaderName() {
        return requestIdResponseHeaderName;
    }

    public void setRequestIdResponseHeaderName(String requestIdResponseHeaderName) {
        String trimVal = StringUtils.trimWhitespace(requestIdResponseHeaderName);
        if (!StringUtils.isEmpty(trimVal)) {
            this.requestIdResponseHeaderName = trimVal;
        }
    }
}
