package com.github.jerryxia.devhelperservletgaesample.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.github.jerryxia.devhelperservletgaesample.service.DefaultHomeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {
    @Autowired
    private DefaultHomeService homeService;

    @RequestMapping("/")
    public ModelAndView index(HttpSession session) {
        ModelAndView mv = new ModelAndView("home/index");

        String requestFirst = "requestFirst";
        // session test
        if (session.getAttribute(requestFirst) == null) {
            String now = DateTime.now(DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss");
            log.info("first request time: {}", now);
            log.info("session class: {}", session.getClass().toString());
            session.setAttribute(requestFirst, now);
        } else {
            log.info("session storage data: {}", session.getAttribute(requestFirst));
        }
        String appName = homeService.query();

        mv.addObject("title", "devhelper-servlet-gae-sample");
        mv.addObject("requestFirst", session.getAttribute(requestFirst));
        mv.addObject("appName", appName);
        return mv;
    }

    @RequestMapping("/now")
    public ModelAndView now() {
        ModelAndView mv = new ModelAndView("home/index");
        mv.addObject("t", DateTime.now(DateTimeZone.UTC));
        return mv;
    }

    @RequestMapping("/error")
    public ModelAndView error(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("error");
        String acceptHeaderValue = request.getHeader("accept");
        if (acceptHeaderValue != null && acceptHeaderValue.indexOf("application/json") > -1) {
            mv.addObject("code", 0);
            mv.addObject("msg", "404 Not Found");
        } else {
            mv.addObject(SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE, "404 Not Found");
        }
        return mv;
    }
}
