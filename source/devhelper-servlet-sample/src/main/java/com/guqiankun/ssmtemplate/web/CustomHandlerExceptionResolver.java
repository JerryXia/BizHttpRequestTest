package com.guqiankun.ssmtemplate.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

public class CustomHandlerExceptionResolver extends SimpleMappingExceptionResolver {
    private static final Logger log = LoggerFactory.getLogger(CustomHandlerExceptionResolver.class);

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        log.error(handler.toString(), ex);
        String viewName = determineViewName(ex, request);
        if (viewName != null) {
            Integer statusCode = determineStatusCode(request, viewName);
            if (statusCode != null) {
                String acceptHeaderValue = request.getHeader("accept");
                if (acceptHeaderValue != null && acceptHeaderValue.indexOf("application/json") > -1) {
                    ModelAndView jsonMv = new ModelAndView();
                    jsonMv.addObject("ret", 0);
                    jsonMv.addObject("msg", ex.getMessage());
                    jsonMv.addObject(DEFAULT_EXCEPTION_ATTRIBUTE, null);
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