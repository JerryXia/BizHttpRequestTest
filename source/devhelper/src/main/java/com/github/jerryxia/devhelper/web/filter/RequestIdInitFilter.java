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
 * <p>请求唯一ID，请求一旦到达API后，API就会生成请求ID并通过响应头返回给客户端。 </p>
 * <p>建议客户端与后端服务都记录此请求ID，可用于问题排查与跟踪。</p>
 * 
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
        filterConfig.getServletContext().log("devhelper RequestIdInitFilter enabled                 : true");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // if convert fail, throw exception
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestId = UUID.randomUUID().toString();
        request.setAttribute(WebConstants.REQUEST_ID_INIT_FILTER_NAME, requestId);
        httpResponse.setHeader(requestIdResponseHeaderName, requestId);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        this.requestIdResponseHeaderName = null;
    }
}