package com.github.jerryxia.devhelperservletgaesample.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class UserIdentityInterceptor extends HandlerInterceptorAdapter {
    private static final String SESSION_NAME = "JSESSIONID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String sessionId = getCookieValue(request.getCookies(), SESSION_NAME);
        if (StringUtils.isEmpty(sessionId)) {
            String[] paramValues = request.getParameterValues(SESSION_NAME);
            if (paramValues != null && paramValues.length > 0) {
                sessionId = paramValues[0];
            }
        }

        if (StringUtils.isEmpty(sessionId)) {
            if (request.getMethod().equals("GET")) {
//                String accept = request.getHeader("accept");
//                if (accept != null && accept.indexOf("json") > -1) {
//                    response.setCharacterEncoding("UTF-8");
//                    response.setContentType("application/json");
//                } else {
//                    response.setCharacterEncoding("UTF-8");
//                    response.setContentType("text/html");
//                }
                request.getRequestDispatcher("/error").forward(request, response);
            } else {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.getWriter().write("{\"code\":0, \"data\": null, \"msg\":\"非法的请求\"}");
            }
            return false;
        }
        return true;
    }

    private String getCookieValue(Cookie[] requestCookies, String cookieName) {
        if (requestCookies != null && requestCookies.length > 0 && cookieName != null && cookieName.length() > 0) {
            for (Cookie cookie : requestCookies) {
                if (cookie != null && cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
