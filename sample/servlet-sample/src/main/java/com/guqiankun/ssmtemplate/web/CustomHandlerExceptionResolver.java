package com.guqiankun.ssmtemplate.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

public class CustomHandlerExceptionResolver extends SimpleMappingExceptionResolver {

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        String viewName = determineViewName(ex, request);
        if (viewName != null) {
            Integer statusCode = determineStatusCode(request, viewName);
            if (statusCode != null) {
                String acceptHeaderValue = request.getHeader("accept");
                if (acceptHeaderValue != null && acceptHeaderValue.indexOf("application/json") > -1) {
                    ModelAndView jsonMv = new ModelAndView();
                    jsonMv.addObject("ret", 0);
                    jsonMv.addObject("msg", ex.getMessage());
                    jsonMv.addObject("exception", null);
                    return jsonMv;
                } else {
                    applyStatusCodeIfPossible(request, response, statusCode);
                }
            }
            return getModelAndView(viewName, ex, request);
        } else {
            return null;
        }
    }
}