/**
 * 
 */
package com.github.jerryxia.devhelper.web.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.github.jerryxia.devhelper.web.WebConstants;

/**
 * @author guqk
 *
 */
public class RequestIdInitFilter implements Filter {
    public static final String PARAM_NAME_REQUEST_ID_RESPONSE_HEADER_NAME = "requestIdResponseHeaderName";

    private String requestIdResponseHeaderName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String cfgResponseHeaderName = filterConfig.getInitParameter(PARAM_NAME_REQUEST_ID_RESPONSE_HEADER_NAME);
        if (cfgResponseHeaderName != null && cfgResponseHeaderName.trim().length() > 0) {
            requestIdResponseHeaderName = cfgResponseHeaderName;
        } else {
            requestIdResponseHeaderName = WebConstants.REQUEST_ID_RESPONSE_HEADER_NAME;
        }
        WebConstants.REQUEST_ID_INIT_FILTER_ENABLED = true;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 请求唯一ID，请求一旦到达API后，API就会生成请求ID并通过响应头返回给客户端
        // 建议客户端与后端服务都记录此请求ID，可用于问题排查与跟
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestId = UUID.randomUUID().toString();
        WebConstants.X_CALL_REQUEST_ID.set(requestId);
        httpResponse.setHeader(requestIdResponseHeaderName, requestId);
        try {
            chain.doFilter(request, response);
        } finally {
            WebConstants.X_CALL_REQUEST_ID.remove();
        }
    }

    @Override
    public void destroy() {

    }
}