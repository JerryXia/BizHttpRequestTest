/**
 * 
 */
package com.github.jerryxia.devhelperspringbootsample.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.utils.time.DateFormatUtil;

import com.github.jerryxia.devutil.SystemClock;
import com.github.jerryxia.devutil.dataobject.web.response.SimpleRes;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 *
 */
@Slf4j
@Controller
public class HomeController {
    @GetMapping("/")
    public ModelAndView index(HttpServletRequest req) {
        String requestFirst = "requestFirst";
        HttpSession session = req.getSession();
        // session test
        if (session.getAttribute(requestFirst) == null) {
            String now = DateFormatUtil.DEFAULT_ON_SECOND_FORMAT.format(SystemClock.nowDate());
            log.info("first request time: {}", now);
            log.info("session: {}", session.getClass().toGenericString());
            session.setAttribute(requestFirst, now);
        } else {
            log.info("session storage data: {}", session.getAttribute(requestFirst));
        }

        String appName = "devhelper-servlet-gae-sample";
        log.debug("appName: {}, now: {}", appName, DateFormatUtil.DEFAULT_ON_SECOND_FORMAT.format(SystemClock.nowDate()));

        ModelAndView mv = new ModelAndView("home/index");
        mv.addObject("appName", appName);
        mv.addObject("requestFirst", session.getAttribute(requestFirst));
        return mv;
    }

    @ResponseBody
    @PostMapping("/now")
    public SimpleRes now() {
        SimpleRes response = new SimpleRes();
        Date now = SystemClock.nowDate();
        if (now.getTime() % 2 == 0) {
            log.warn("random warn message, {}", now);
        } else {
            log.error("random error message, {}", now);
        }
        response.getData().put("t", DateFormatUtil.DEFAULT_ON_SECOND_FORMAT.format(now));
        return response;
    }
}
