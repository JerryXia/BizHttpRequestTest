package com.guqiankun.ssmtemplate.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest httpRequest) {
        ModelAndView mv = new ModelAndView("home/index");
        mv.addObject("currDateTime", org.joda.time.DateTime.now(DateTimeZone.UTC));
        logger.debug("id:{}", httpRequest.getSession().getId());
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
            mv.addObject("exception", "404 Not Found");
        }
        return mv;
    }
}
