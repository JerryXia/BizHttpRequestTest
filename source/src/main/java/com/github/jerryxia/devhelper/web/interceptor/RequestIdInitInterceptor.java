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
    // response header name defaultValue: X-Call-RequestId
    private String responseHeaderName = WebConstants.X_CALL_REQUEST_ID.toString();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 请求唯一ID，请求一旦到达API后，API就会生成请求ID并通过响应头返回给客户端
        // 建议客户端与后端服务都记录此请求ID，可用于问题排查与跟
        String requestId = UUID.randomUUID().toString();
        WebConstants.X_CALL_REQUEST_ID.set(requestId);
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
        WebConstants.X_CALL_REQUEST_ID.remove();
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
