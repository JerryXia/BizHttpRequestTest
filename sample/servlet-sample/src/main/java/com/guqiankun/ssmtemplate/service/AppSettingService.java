package com.guqiankun.ssmtemplate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.guqiankun.ssmtemplate.mapper.AppSettingMapper;
import com.guqiankun.ssmtemplate.model.po.AppSetting;

@Service
public class AppSettingService {

    @Autowired
    private AppSettingMapper appSettingMapper;

    @Cacheable(value = "redis")
    public List<AppSetting> customQueryAll() {
        return appSettingMapper.selectAll();
    }

    public List<AppSetting> queryAllForPagedList() {
        return appSettingMapper.selectAll();
    }
}