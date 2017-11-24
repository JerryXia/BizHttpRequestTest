package com.guqiankun.ssmtemplate.web.interceptor;

import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.guqiankun.ssmtemplate.common.SysThreadLocal;

public class UserIdentityTokenInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] requestCookies = request.getCookies();
        if (requestCookies != null && requestCookies.length > 0) {
            for (Cookie cookie : requestCookies) {
                // user token
                if (cookie != null && cookie.getName().equals("ut") && !StringUtils.isEmpty(cookie.getValue())) {
                    String userIdentityToken = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    //TODO: 检查user
                }
            }
        }
        if (request.getMethod().equals("GET")) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            request.getRequestDispatcher("/WEB-INF/jsp/user/notlogined.jsp").forward(request, response);
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write("{\"code\":0, \"data\": null, \"msg\":\"非法的请求\"}");
        }
        return false;
    }
}
