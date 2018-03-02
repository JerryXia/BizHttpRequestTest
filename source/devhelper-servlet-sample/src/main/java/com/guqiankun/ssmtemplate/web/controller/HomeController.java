package com.guqiankun.ssmtemplate.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

@Controller
public class HomeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        ModelAndView mv = new ModelAndView("home/index");
        mv.addObject("currDateTime", org.joda.time.DateTime.now(DateTimeZone.UTC));
        logger.debug("id:{}", httpRequest.getSession().getId());

        try {
            httpRequest.getRequestDispatcher("/forward.jsp").forward(httpRequest, httpResponse);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mv;
    }


    @RequestMapping("/throwerror")
    public ModelAndView throwError() {
        ModelAndView mv = new ModelAndView("home/index");
        mv.addObject("currDateTime", org.joda.time.DateTime.now(DateTimeZone.UTC));
        int i = 0;
        if(i == 0) {
            throw new IllegalArgumentException("uncached error");
        }
        return mv;
    }

    
    @RequestMapping("/error")
    public ModelAndView error(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("error");
        String acceptHeaderValue = request.getHeader("accept");
        if (acceptHeaderValue != null && acceptHeaderValue.indexOf("application/json") > -1) {
            mv.addObject("ret", 0);
            mv.addObject("msg", "404 Not Found");
        } else {
            mv.addObject(SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE, "404 Not Found");
        }
        return mv;
    }
}
