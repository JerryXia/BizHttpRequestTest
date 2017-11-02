package com.guqiankun.ssmtemplate.web.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.guqiankun.ssmtemplate.common.SysThreadLocal;

/**
 * 用于为每次请求初始化一个唯一id
 */
public class RequestInitInterceptor extends HandlerInterceptorAdapter {
    // 相应头默认值
    private String responseHeaderName = "X-Call-RequestId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 请求唯一ID，请求一旦到达API后，API就会生成请求ID并通过响应头返回给客户端
        // 建议客户端与后端服务都记录此请求ID，可用于问题排查与跟
        String requestId = UUID.randomUUID().toString();
        SysThreadLocal.RequestId.set(requestId);
        response.setHeader(responseHeaderName, requestId);
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
        SysThreadLocal.RequestId.remove();
    }

    public String getResponseHeaderName() {
        return responseHeaderName;
    }

    public void setResponseHeaderName(String responseHeaderName) {
        String trimVal = StringUtils.trimWhitespace(responseHeaderName);
        if (!StringUtils.isEmpty(trimVal)) {
            this.responseHeaderName = trimVal;
        }
    }
}