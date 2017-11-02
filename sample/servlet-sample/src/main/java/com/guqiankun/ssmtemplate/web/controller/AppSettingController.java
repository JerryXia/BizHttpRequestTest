package com.guqiankun.ssmtemplate.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.guqiankun.ssmtemplate.model.po.AppSetting;
import com.guqiankun.ssmtemplate.model.vo.AppSetting.ListRequest;
import com.guqiankun.ssmtemplate.service.AppSettingService;

@Controller
@RequestMapping("/appSetting")
public class AppSettingController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AppSettingController.class);

    @Autowired
    private AppSettingService appSettingService;

    @RequestMapping("/list")
    public ModelAndView list(@Valid ListRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return getMvNotFound("参数有误");
        } else {
            ModelAndView mv = getMvOk("appSetting/list");
            List<AppSetting> appSettings = appSettingService.customQueryAll();
            mv.addObject("title", "title");
            mv.addObject("appSettings", appSettings);
            return mv;
        }
    }

    @RequestMapping(value = "/pageList")
    public ModelAndView pageList(
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int limit) {
        logger.info("offset:{}, limit:{}", offset, limit);
        ModelAndView mv = getMvOk("appSetting/pageList");
        PageHelper.offsetPage(offset, limit, true);
        List<AppSetting> pagedList = appSettingService.queryAllForPagedList();
        mv.addObject("offset", offset);
        mv.addObject("limit", limit);
        mv.addObject("pageInfo", new PageInfo<AppSetting>(pagedList));
        return mv;
    }
}
