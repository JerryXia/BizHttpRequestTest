package com.guqiankun.ssmtemplate.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

public class BaseController {

    protected ModelAndView getMvNotFound(String msg) {
        ModelAndView source = new ModelAndView("error");
        source.setStatus(HttpStatus.NOT_FOUND);
        source.addObject("ret", 0);
        source.addObject("msg", msg);
        source.addObject("exception", msg);
        return source;
    }


    protected ModelAndView getMvOk(String viewName) {
        ModelAndView source = new ModelAndView(viewName);
        source.addObject("ret", 1);
        //source.addObject("msg", msg);
        return source;
    }

}
