package com.guqiankun.ssmtemplate.mapper;

import java.util.List;

import com.guqiankun.ssmtemplate.model.po.AppSetting;

public interface AppSettingMapper {
    int insert(AppSetting record);

    List<AppSetting> selectAll();

}